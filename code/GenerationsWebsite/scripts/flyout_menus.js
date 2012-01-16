
/*
 * Responsible for flyout menus within the editor + on a published page.
 * Also responsible for condensing overflowing nav and making a "more..." link.
 *
 * Author: Adam Shaw
 */
 
(function() {

	/****************************** publically available functions ****************************/
	
	var moreItemHTML;
	var activeLiId;
	var currentPageId;
	
	// called from a published page
	window.initPublishedFlyoutMenus = function(topLevelSummary, cpid, moreItemHTML, aLiId, isPreview) {
		currentPageId = cpid;
		if (topLevelSummary.length > 0) {
			var go = function() {
				activeLiId = aLiId;
				var container = document.createElement('div');
				container.id = 'weebly-menus';
				$(document.body).insert(container);
				var firstItem = navElm(topLevelSummary[0].id);
				if (firstItem) {
					window.navFlyoutMenu = new FlyoutMenu(firstItem.up(), {
						relocate: container,
						aLiId: aLiId
					});
					condenseNav(topLevelSummary, moreItemHTML);
				}
			}
			if (isPreview) {
				go(); // css has been written in html <style> tag, no need to check if loaded
			}else{
				whenThemeCSSLoaded(go);
			}
		}
	};
	
	// called from the editor
	window.initEditorFlyoutMenus = function() {
		whenThemeCSSLoaded(function() {
			function go() {
				var topLevelSummary = Weebly.PageManager.getTopLevelSummary();
				if (topLevelSummary.length > 0) {
					var listItem0 = navElm(topLevelSummary[0].id);
					if (listItem0) {
						var listElement = listItem0.up();
						if (listElement && listElement.nodeName && !listElement.nodeName.match(/(table|tbody|thead|tr)/i)) {
							window.navFlyoutMenu = new FlyoutMenu(listElement, {
								relocate: 'weebly-menus'
							});
							moreItemHTML = currentThemeDefinition['menuRegular'];
							moreItemHTML = moreItemHTML.replace('%%MENUITEMLINK%%', '#');
							moreItemHTML = moreItemHTML.replace('%%MENUITEMTITLE%%', /*tl(*/'more...'/*)tl*/);
							moreItemHTML = "<span class='weebly-nav-handle weebly-nav-more'>" + moreItemHTML + "</span>";
							condenseNav(topLevelSummary, moreItemHTML);
						}else{
							window.navFlyoutMenu = null;
						}
					}else{
						window.navFlyoutMenu = null;
					}
				}else{
					window.navFlyoutMenu = null;
				}
			}
			if (Prototype.Browser.WebKit) {
				// this solves a webkit bug where the <span>s within the <ul> are displayes as block
				// this problem has nothing to do with the flyout code, but this was the most convenient place to put it ~ashaw
				var handles = $$('#icontent span.weebly-nav-handle');
				handles.each(Element.hide);
				setTimeout(function() {
					handles.each(Element.show);
					go();
				},0);
			}else{
				go();
			}
		});
	};
	
	// called from the editor when nav positioning might have changed
	window.refreshNavCondense = function(callback) {
		if (window.navFlyoutMenu) {
			//console.log('refresh');
			condenseNav(Weebly.PageManager.getTopLevelSummary(), moreItemHTML);
		}
	};
	
	window.disableFlyouts = false;
	
	
	
	/*********************************** flyout menu class ************************************/

	window.FlyoutMenu = function(mainList, options) {

		mainList = $(mainList); // the element that contains all the nav elements
		options = options || {};
		
		// settings (an attempt at making FlyoutMenu portable)
		var listTag = options.listTag ? options.listTag.toLowerCase() : 'ul';
		var itemTag = options.itemTag ? options.itemTag.toLowerCase() : 'li';
		var delay = (options.delay || 0.5) * 1000;
		var slideDuration = options.slideDuration || 0.3;
		
		// if specified, all submenus will be detached from original place in DOM and put in here
		var relocate = options.relocate ? $(options.relocate) : false;
		
		// FYI
		// a 'handle' is an element that contains the templatable HTML for each page's nav link
		// a 'handle' may be a wrapping SPAN element (with className 'weebly-nav-handle')
		//   OR it may be the item itself (such as an LI)
		
		var allItems; // list of all nav items
		              // (the first child within a handle OR the handle itself)
		
		
		//
		// attach all event handlers and do state-keeping for flyout menus
		//
		
		function initItem(item) {
		
			item.style.position = 'relative'; // this gives more accurate offsets
			var innerAs = item.getElementsByTagName('a');
			if (innerAs.length) {
				innerAs[0].style.position = 'relative'; // more accurate offset (prevents IE bug)
			}
	
			// states
			var isSliding = false;
			var isExpanded = false;
			var isMouseoverItem = false;
			var mouseoverCnt = 0;
			
			var slidVertically = false;
			var slidRight = false;
		
			var sublistWrapper; // a DIV.weebly-menu-wrap OR null
			var sublist;        // a UL.weebly-menu OR null
			
			var currentEffect;
			
			
			//
			// expand a sublist on mouseover
			//
			
			function itemMouseover() {
				if (disableFlyouts) return false;
				mouseoverCnt++;
				isMouseoverItem = true;
				if (!isExpanded && !isSliding) {
					if (sublist) {
						// when a sublist is expanded, immediately contract all siblings' sublists
						getSiblings(item).each(function(sib) {
							if (sib._flyoutmenu_contract) {
								sib._flyoutmenu_contract();
							}
						});
						expandSublist();
					}
				}
			}
			
			
			//
			// contract sublist on mouseout (after delay)
			//
			
			function itemMouseout() {
				if (disableFlyouts) return false;
				isMouseoverItem = false;
				if (isExpanded) {
					var mouseoverCnt0 = mouseoverCnt;
					setTimeout(function() {
						if (mouseoverCnt == mouseoverCnt0 && isExpanded && !isSliding) {
							contractSublist();
						}
					}, delay);
				}
			}
			
			
			//
			// prevent contracting when sublist is moused over
			//
			
			function sublistWrapperMouseover() {
				if (disableFlyouts) return false;
				mouseoverCnt++;
			}
			
			
			//
			// do item's sublist's expand animation
			//
		
			function expandSublist() {
				isSliding = true;
				var opts = {
					duration: slideDuration,
					afterFinish: function() { // when animation has finished
						currentEffect = null;
						isSliding = false;
						isExpanded = true;
						if (!isMouseoverItem) {
							// if mouse was not over when animation finished, immediately contract
							contractSublist();
						}else{
							// attach methods for later hiding/contracting
							item._flyoutmenu_contract = contractSublist;
							item._flyoutmenu_hide = function() {
								isSliding = false;
								isExpanded = false;
								isMouseoverItem = false;
								item._flyoutmenu_contract = null;
								item._flyoutmenu_hide = null;
								sublistWrapper.hide();
							};
						}
					}
				};
				var massCoords = getItemMassCoords(item);
				var localOrigin = sublistWrapper.getOffsetParent().cumulativeOffset();
				if (inVerticalList(item, true, options.aLiId)) {
					// slide right on vertical nav
					slidVertically = false;
					sublistWrapper.style.top = -localOrigin.top + massCoords[0].top + 'px';
					var w = sublistWrapper.getWidth();
					if (massCoords[1].left + w > $(document.body).getWidth()) {
						slidRight = false;
						sublistWrapper.style.left = -localOrigin.left + massCoords[0].left - w + 'px';
						// currentEffect = Effect.SlideLeftIn(sublistWrapper, opts);
						// jakewent's SlideLeftIn sucks... just show it
						sublistWrapper.show();
						opts.afterFinish();
					}else{
						slidRight = true;
						sublistWrapper.style.left = -localOrigin.left + massCoords[1].left + 'px';
						currentEffect = Effect.SlideRightIn(sublistWrapper, opts);
					}
				}else{
					// slide down on horizontal nav
					slidVertically = true;
					sublistWrapper.style.top = -localOrigin.top + massCoords[1].top + 'px';
					var w = sublistWrapper.getWidth();
					if (massCoords[0].left + w > $(document.body).getWidth()) {
						slidRight = false;
						sublistWrapper.style.left = -localOrigin.left + massCoords[1].left - w + 'px';
					}else{
						slidRight = true;
						sublistWrapper.style.left = -localOrigin.left + massCoords[0].left + 'px';
					}
					currentEffect = Effect.SlideDown(sublistWrapper, opts);
				}
			}
			
			
			//
			// do item's sublist's contract animation
			//
		
			function contractSublist(mouseoverHack) {
				if (disableFlyouts || !item.parentNode) { // no parentNode?? removed from dom already? wtf!?
					// contractSublist is often called from a delay, might have been disabled in that time
					return;
				}
				if (mouseoverHack) {
					// IE6 wasn't registering the mouseout
					isMouseoverItem = false;
				}
				isSliding = true;
				item._flyoutmenu_contract = null;
				item._flyoutmenu_hide = null;
				var opts = {
					duration: slideDuration,
					afterFinish: function() {
						currentEffect = null;
						isSliding = false;
						isExpanded = false;
						if (isMouseoverItem) {
							// if mouseleft, but re-entered before animation finished
							// immediately expand sublist again
							expandSublist();
						}
					}
				}
				if (slidVertically) {
					currentEffect = Effect.SlideUp(sublistWrapper, opts);
				}else{
					if (slidRight) {
						currentEffect = Effect.SlideLeftOut(sublistWrapper, opts);
					}else{
						currentEffect = Effect.SlideRightOut(sublistWrapper, opts);
					}
				}
			}
			
			
			//
			// initialize submenu and attach events
			//
			
			sublist = getSublist(item, listTag);
			if (sublist) {
				sublistWrapper = sublist.up();
				sublistWrapper.style.position = 'absolute';
				sublistWrapper.hide(); // should already be display:none, but just in case
				if (relocate) {
					// since sublist is no longer a descendant of the item, mouse events
					// wont cascade. simulate this
					sublistWrapper.observe('mouseover', itemMouseover);
					sublistWrapper.observe('mouseout', itemMouseout);
				}else{
					// keep the submenu alive...
					sublistWrapper.observe('mouseover', sublistWrapperMouseover);
				}
			}
			item.observe('mouseover', itemMouseover);
			item.observe('mouseout', itemMouseout);
			
			
			//
			// attach a method for removing registered events
			// (returns the sublist wrapper)
			//
			
			item._flyoutmenu_destroy = function(removeSublist) {
				if (currentEffect) {
					// effect is still animating, kill it now
					currentEffect.cancel();
					currentEffect = null;
				}
				item.stopObserving('mouseover', itemMouseover);
				item.stopObserving('mouseout', itemMouseout);
				if (removeSublist) {
					if (sublistWrapper) {
						return sublistWrapper.remove(); // detach before returning
					}
				}
				else if (sublistWrapper) {
					if (relocate) {
						sublistWrapper.stopObserving('mouseover', itemMouseover);
						sublistWrapper.stopObserving('mouseout', itemMouseout);
					}else{
						sublistWrapper.stopObserving('mouseover', sublistWrapperMouseover);
					}
					return sublistWrapper;
				}
			};
				
		}
		
		
		//
		// methods for the FlyoutMenu object
		//
		
		// close all submenus with an animation
		this.contract = function() {
			allItems.each(function(item) {
				if (item._flyoutmenu_contract) {
					item._flyoutmenu_contract(true);
				}
			});
		};
		
		// hide all submenus immediately
		this.hideSubmenus = function() {
			allItems.each(function(item) {
				if (item._flyoutmenu_hide) {
					item._flyoutmenu_hide();
				}
			});
		};
		
		// detach all event handlers
		this.destroy = function() {
			allItems.each(function(item) {
				if (item._flyoutmenu_destroy) {
					item._flyoutmenu_destroy();
				}
			});
		};
		
		// initialize a top level item that has already been placed into mainList
		this.addItem = function(handle) { // todo: rename initTopLevelItem()
			var item = getRealTopLevelItem(handle);
			if (item) {
				initItem(item);
				var sublist = getSublist(item, listTag);
				if (sublist) {
					sublist.select(itemTag).each(initItem); // init all subitems
				}
				if (relocate && sublist) {
					relocate.insert(sublist.parentNode); // relocate sublist's wrap
				}
				allItems.push(item);
			}
		};
		
		// detach an item's event handlers and remove from DOM
		this.removeItem = function(handle) { // todo: rename
			var item = getRealTopLevelItem(handle);
			if (item) {
				if (item._flyoutmenu_destroy) {
					item._flyoutmenu_destroy(true);
				}
				item.remove();
				allItems = allItems.without(item);
			}
		};
		
		// accessor
		this.getMainList = function() {
			return mainList;
		};
		
		
		//
		// initialize allItems and relocate
		//
		
		allItems = getAllItems(mainList, itemTag);
		allItems.each(initItem);
		
		if (relocate) {
			getTopLevelItems(mainList).each(function(item) {
				var sublist = getSublist(item, listTag);
				if (sublist) {
					relocate.insert(sublist.parentNode);
				}
			});
		}

	};
	
	
	
	
	
	/****************************** more... link and menu *****************************/
	
	function condenseNav(topLevelSummary, moreItemHTML) { // can be called repeatedly for updating
		if (window.DISABLE_NAV_MORE) return;
		//console.log('condenseNav');
		var cpid = window.currentPage || currentPageId;
		var mainList = navFlyoutMenu.getMainList();
		var mainListChildren = mainList.childElements();
		var moreHandle;
		if (mainListChildren.length > 0) {
			moreHandle = mainListChildren[mainListChildren.length-1];
			if (!moreHandle.hasClassName('weebly-nav-more')) {
				moreHandle = null;
			}
		}
		var alreadyMore = false;
		if (moreHandle) {
			moreHandle.hide();
			alreadyMore = true;
		}
		var isVertical;
		var handles = []; // holds all the handles up til the breaking element
		var itemCoords = [];
		var breakingHandle;
		var breakingIndex;
		for (var i=0; i<topLevelSummary.length; i++) {
			var handle = navElm(topLevelSummary[i].id);
			if (alreadyMore) {
				handle.show();
			}
			var item = getRealTopLevelItem(handle);
			if (!item) continue;
			var coords = getItemMassCoords(item);
			if (i == 1) {
				isVertical = Math.abs(coords[0].top - itemCoords[0][0].top) > Math.abs(coords[0].left - itemCoords[0][0].left);
			}
			else if (i > 1 && !isVertical && Math.abs(coords[0].top - itemCoords[i-1][0].top) > 5) {
				breakingHandle = handle;
				breakingIndex = i;
				break;
			}
			handles.push(handle);
			itemCoords.push(coords);
		}
		if (breakingHandle) {
			if (moreHandle) {
				moreHandle.show();
			}else{
				var temp = $(document.createElement('div'));
				temp.innerHTML = moreItemHTML;
				moreHandle = temp.down();
				moreHandle.select('a').each(function(moreAnchor) {
					moreAnchor.onclick = function() { return false; };
					moreAnchor.style.position = 'relative'; // match what initItem does
					moreAnchor.id = 'weebly-nav-more-a';
				});
				mainList.insert(moreHandle);
			}
			var moreItem = getRealTopLevelItem(moreHandle);
			moreItem.style.position = 'relative'; // match what initItem does
			var hiddenItemIndices = [];
			for (var i=breakingIndex; i<topLevelSummary.length; i++) {
				navElm(topLevelSummary[i].id).hide();
				hiddenItemIndices.push(i);
			}
			for (var i=breakingIndex-1; i>=0; i--) {
				var moreCoords = getItemMassCoords(moreItem);
				if (Math.abs(moreCoords[0].top - itemCoords[i][0].top) > 5) {
					handles[i].hide();
					hiddenItemIndices.unshift(i);
				}else{
					break;
				}
			}
			if (hiddenItemIndices.length == 0) {
				// no items were hidden, no need for more...
				moreHandle.remove();
			}
			else if (hiddenItemIndices.length == topLevelSummary.length) {
				// all items were hidden, revert back
				for (var i=0; i<hiddenItemIndices.length; i++) {
					navElm(topLevelSummary[hiddenItemIndices[i]].id).show();
				}
				moreHandle.remove();
			}
			else {
				if (!alreadyMore) {
					var wrap = $(document.createElement('div'));
					wrap.addClassName('weebly-menu-wrap');
					var ul = $(document.createElement('ul'));
					ul.addClassName('weebly-menu');
					wrap.appendChild(ul);
					for (var j=0; j<hiddenItemIndices.length; j++) {
						var pageSummary = topLevelSummary[hiddenItemIndices[j]];
						var li = $(document.createElement('li'));
						li.id = 'weebly-nav-' + pageSummary.id;
						if (pageSummary.id == cpid) {
							li.addClassName('weebly-nav-current');
						}
						var a = $(document.createElement('a'));
						if (pageSummary.onclick) {
							a.href = '#';
							a.onclick = pageSummary.onclick;
						}else{
							a.href = '/' + pageSummary.url; // TODO: 'url' is misleading
						}
						li.appendChild(a);
						var submenu = getRealTopLevelItem(navElm(topLevelSummary[hiddenItemIndices[j]].id))._flyoutmenu_destroy();
						a.innerHTML =
							"<span class='weebly-menu-title'>" + pageSummary.title + "</span>" +
							(submenu ? "<span class='weebly-menu-more'>&gt;</span>" : '');
						if (submenu) {
							li.appendChild(submenu);
						}
						ul.appendChild(li);
					}
					moreItem.appendChild(wrap);
					navFlyoutMenu.addItem(moreItem);
					if (window.showEvent) {
						showEvent('navMore');
					}
				}
			}
		}
	}
	
	
	
	
	
	/************************ helpers for navigating and querying items/sublists/etc ********************/
	
	function inVerticalList(item, strict, aLiId) {
		var list = item.up();
		if (list.hasClassName('weebly-nav-handle')) {
			list = list.up();
		}
		var allItems = getTopLevelItems(list, strict, aLiId);
		if (allItems.length >= 2) {
			var o1 = allItems[0].positionedOffset();
			var o2 = allItems[1].positionedOffset();
			var diff = Math.abs(o1.left - o2.left) - Math.abs(o1.top - o2.top);
			if (diff != 0) {
				return diff < 0;
			}
		}
		return !isItemTopLevel(item);
			// default to returning false for top level user-defined css
			// and true for weebly-created submenus
	}
	
	function getTopLevelItems(list, strict, aLiId) {
		var res = [];
		list.childElements().each(function(handle) {
			if (!strict ||
				handle.hasClassName('weebly-nav-handle') ||
				handle.hasClassName('weebly-nav-more') ||
				handle.id.match(/^pg/) ||
				(aLiId && handle.id==aLiId)) {
					var item = getRealTopLevelItem(handle);
					if (item) {
						res.push(item);
					}
				}
		});
		return res;
	}
	
	function getRealTopLevelItem(item) { // todo: rename to getItemFromHandle()
		if (item.hasClassName('weebly-nav-handle')) {
			item = item.down();
		}
		if (item && !item.hasClassName('weebly-menu-wrap')) {
			// sometimes with SPAN handles, markup was invalid and DOM messed up
			// so make sure item is not a menu
			return item;
		}
	}
	
	function getAllItems(list, itemTag) {
		// get top level and all descendant items
		return list.select(itemTag).concat(getTopLevelItems(list)).uniq();
	}
	
	function getSiblings(item) {
		if (item.parentNode.hasClassName('weebly-nav-handle')) {
			var siblings = [];
			item.up().siblings().each(function(handle) {
				var sib = handle.down();
				if (sib) {
					siblings.push(sib);
				}
			});
			return siblings;
		}else{
			// items aren't wrapped by separate handles
			return item.siblings();
		}
	}
	
	function getSublist(item, listTag) {
		var sublist = item.down(listTag);
		if (!sublist) {
			var next = item.next();
			if (next && next.hasClassName('weebly-menu-wrap')) {
				// sometimes with SPAN handles, markup is invalid, and it
				// makes the sublist a sibling AFTER the item
				sublist = next.down();
			}
		}
		return sublist;
	}
	
	function isItemTopLevel(item) {
		var list = item.up();
		if (list.hasClassName('weebly-nav-handle')) {
			list = list.up();
		}
		return !list.hasClassName('weebly-menu');
	}
	
	function getItemMassCoords(item) {
		// look at the item and its A tag and return the largest rectangle of space it takes up
		var anchor = item.nodeName == 'A' ? item : $(item.getElementsByTagName('a')[0]);
		var p1 = item.cumulativeOffset();
		var p2 = { top:p1.top+item.getHeight(), left:p1.left+item.getWidth() };
		if (!anchor) {
			// messed up DOM (SPAN's around TD's and such) sometimes pushes A tag outside of item
			return [p1, p2];
		}
		var p3 = anchor.cumulativeOffset();
		var p4 = { top:p3.top+anchor.getHeight(), left:p3.left+anchor.getWidth() };
		var p5, p6;
		if (Math.abs(p1.left - p2.left) < 10) { // a tag is really small, doen't have any mass..
			// the inner A tag is probably floated and the LI isn't. lame. just use A tag's coords
			p5 = p3;
			p6 = p4;
		}else{
			p5 = { top:Math.min(p1.top, p3.top), left:Math.min(p1.left, p3.left) };
			p6 = { top:Math.max(p2.top, p4.top), left:Math.max(p2.left, p4.left) };
		}
		return [p5, p6];
	}
	
	function navElm(id) { // todo: rename to getHandle()
		var elm = $('pg'+id);
		if (elm) return elm;
		if (activeLiId) return $(activeLiId);
	}
	
	
	
	
	/************************** helpers for theme-css-loaded detection ***********************/
	
	function isThemeCSSLoaded() {
		for (var i=0; i<document.styleSheets.length; i++) {
			try {
				if (document.styleSheets[i].title == 'weebly-theme-css') {
					var sheet = document.styleSheets[i];
					var rules = sheet.cssRules || sheet.rules;
					return rules && rules.length > 0;
				}
			}
			catch (err) {}
		}
		return false;
	}
	
	function whenThemeCSSLoaded(callback) {
		if (isThemeCSSLoaded()) {
			callback();
		}else{
			var iters = 0;
			var maxIters = 10;
			var intervalID = setInterval(function() {
				if (++iters > maxIters) {
					clearInterval(intervalID);
				}
				else if (isThemeCSSLoaded()) {
					clearInterval(intervalID);
					callback();
				}
			}, 200);
		}
	}

})();



/******************************* extra scriptaculous effects required for fly-out ****************************/
// http://scriptaculous.jakewendt.com/


Effect.SlideRightOut = function(element) {
/* 
	SlideRightOut need to have the content of the element wrapped in a container element with fixed width!
*/
	element = $(element).cleanWhitespace();
	var elementDimensions = element.getDimensions();
	return new Effect.Parallel ( [
		new Effect.Move(element, { x: element.getWidth(), sync: true, mode: 'relative' }),
		new Effect.Scale(element, window.opera ? 0 : 1, {	
			sync: true, 
			scaleContent: false, 
			scaleY: false,
			scaleFrom: 100,
			restoreAfterFinish: true
		})
		], Object.extend({ 
			beforeSetup: function(effect){
				effect.effects[0].element.makeClipping();
			},
			afterFinishInternal: function(effect){
				effect.effects[0].element.undoClipping().hide();
			}
		}, arguments[1] || {})
	);
}



/* from SlideUp */
Effect.SlideLeftOut = function(element) {
/*
	SlideLeftOut needs to have the content of the element wrapped in a container element with fixed width
	otherwise any text or images begin to wrap in stange ways!
*/
	element = $(element).cleanWhitespace();
	return new Effect.Scale(element, window.opera ? 0 : 1,
		Object.extend({ 
			scaleContent: false, 
			scaleY: false, 
			scaleMode: 'box',
			scaleFrom: 100,
			restoreAfterFinish: true,
			beforeStartInternal: function(effect) {
				effect.element.makePositioned();
				effect.element.down().makePositioned();
				if(window.opera) effect.element.setStyle({left: ''});
				effect.element.makeClipping().show();
			},  
			afterUpdateInternal: function(effect) {
				var down = effect.element.down();
				if (down) {
					// todo: add comment here
					down.setStyle(
						{right: (effect.dims[1] - effect.element.clientWidth) + 'px' }
					);
				}
			},
			afterFinishInternal: function(effect) {
				effect.element.hide().undoClipping().undoPositioned();
				var down = effect.element.down();
				if (down) {
					down.undoPositioned();
				}
			}
		}, arguments[1] || {})
	);
}


/* from SlideDown */
Effect.SlideRightIn = function(element) {
/*
	SlideRightIn needs to have the content of the element wrapped in a container element with fixed width!
*/
	element = $(element).cleanWhitespace();
	var elementDimensions = element.getDimensions();
	return new Effect.Scale(element, 100, 
		Object.extend({ 
			scaleContent: false, 
			scaleY: false, 
			scaleFrom: window.opera ? 0 : 1,
			scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
			restoreAfterFinish: true,
			afterSetup: function(effect) {
				effect.element.makePositioned();
				effect.element.down().makePositioned();
				if(window.opera) effect.element.setStyle({left: ''});
				effect.element.makeClipping().setStyle({width: '0px'}).show(); 
			},
			afterUpdateInternal: function(effect) {
				effect.element.down().setStyle({right: (effect.dims[1] - effect.element.clientWidth) + 'px' }); 
			},
			afterFinishInternal: function(effect) {
				effect.element.undoClipping().undoPositioned();
				effect.element.down().undoPositioned();
			}
		}, arguments[1] || {})
	);
}

