/**
 Script: Slideshow.js
 Slideshow - A javascript class for Mootools to stream and animate the presentation of images on your website.

 License:
 MIT-style license.

 Copyright:
 Copyright (c) 2008 [Aeron Glemann](http://www.electricprism.com/aeron/).

 Dependencies:
 Mootools 1.2 Core: Fx.Morph, Fx.Tween, Selectors, Element.Dimensions.
 Mootools 1.2 More: Assets.
 */

Slideshow = new Class({Implements:[Chain,Events,Options],options:{captions:false,center:true,classes:[],controller:false,delay:5000,duration:750,fast:false,height:false,href:"",hu:"",linked:false,loader:{animate:["css/loader-#.png",12]},loop:true,match:/\?slide=(\d+)$/,overlap:true,paused:false,properties:["href","rel","rev","title"],random:false,replace:[/(\.[^\.]+)$/,"t$1"],resize:"width",slide:0,thumbnails:false,titles:true,transition:function(A) {
    return -(Math.cos(Math.PI * A) - 1) / 2
},width:false},initialize:function(C, F, L) {
    this.setOptions(L);
    this.slideshow = $(C);
    if (!this.slideshow) {
        return
    }
    this.slideshow.set("styles", {display:"block",position:"relative","z-index":0});
    var H = window.location.href.match(this.options.match);
    this.slide = (this.options.match && H) ? H[1].toInt() : this.options.slide;
    this.counter = this.delay = this.transition = 0;
    this.direction = "left";
    this.paused = false;
    if (!this.options.overlap) {
        this.options.duration *= 2
    }
    var G = this.slideshow.getElement("a") || new Element("a");
    if (!this.options.href) {
        this.options.href = G.get("href") || ""
    }
    if (this.options.hu.length && !this.options.hu.test(/\/$/)) {
        this.options.hu += "/"
    }
    if (this.options.fast === true) {
        this.options.fast = 2
    }
    var K = ["slideshow","first","prev","play","pause","next","last","images","captions","controller","thumbnails","hidden","visible","inactive","active","loader"];
    var J = K.map(function(N, M) {
        return this.options.classes[M] || N
    }, this);
    this.classes = J.associate(K);
    this.classes.get = function() {
        var O = "." + this.slideshow;
        for (var N = 0,M = arguments.length; N < M; N++) {
            O += ("-" + this[arguments[N]])
        }
        return O
    }.bind(this.classes);
    if (!F) {
        this.options.hu = "";
        F = {};
        var D = this.slideshow.getElements(this.classes.get("thumbnails") + " img");
        this.slideshow.getElements(this.classes.get("images") + " img").each(function(O, Q) {
            var S = O.get("src");
            var N = $pick(O.get("alt"), O.get("title"), "");
            var R = O.getParent();
            var P = (R.get("tag") == "a") ? R.getProperties : {};
            var M = O.getParent().get("href") || "";
            var T = (D[Q]) ? D[Q].get("src") : "";
            F[S] = {caption:N,href:M,thumbnail:T}
        })
    }
    var E = this.load(F);
    if (!E) {
        return
    }
    this.events = $H({keydown:[],keyup:[],mousemove:[]});
    var B = function(M) {
        switch (M.key) {case"left":this.prev(M.shift);break;case"right":this.next(M.shift);break;case"p":this.pause();break
        }
    }.bind(this);
    this.events.keyup.push(B);
    document.addEvent("keyup", B);
    var C = this.slideshow.getElement(this.classes.get("images"));
    var I = (C) ? C.empty() : new Element("div", {"class":this.classes.get("images").substr(1)}).inject(this.slideshow);
    var A = I.getSize();
    this.height = this.options.height || A.y;
    this.width = this.options.width || A.x;
    I.set({styles:{display:"block",height:this.height,overflow:"hidden",position:"relative",width:this.width}});
    this.slideshow.store("images", I);
    this.a = this.image = this.slideshow.getElement("img") || new Element("img");
    if (Browser.Engine.trident && Browser.Engine.version > 4) {
        this.a.style.msInterpolationMode = "bicubic"
    }
    this.a.set("styles", {display:"none",position:"absolute",zIndex:1});
    this.b = this.a.clone();
    [this.a,this.b].each(function(M) {
        G.clone().cloneEvents(G).grab(M).inject(I)
    });
    if (this.options.captions) {
        this._captions()
    }
    if (this.options.controller) {
        this._controller()
    }
    if (this.options.loader) {
        this._loader()
    }
    if (this.options.thumbnails) {
        this._thumbnails()
    }
    this._preload()
},go:function(B, A) {
    if ((this.slide - 1 + this.data.images.length) % this.data.images.length == B || $time() < this.transition) {
        return
    }
    $clear(this.timer);
    this.delay = 0;
    this.direction = (A) ? A : ((B < this.slide) ? "right" : "left");
    this.slide = B;
    if (this.preloader) {
        this.preloader = this.preloader.destroy()
    }
    this._preload(this.options.fast == 2 || (this.options.fast == 1 && this.paused))
},first:function() {
    this.prev(true)
},prev:function(A) {
    var B = 0;
    if (!A) {
        if (this.options.random) {
            if (this.showed.i < 2) {
                return
            }
            this.showed.i -= 2;
            B = this.showed.array[this.showed.i]
        } else {
            B = (this.slide - 2 + this.data.images.length) % this.data.images.length
        }
    }
    this.go(B, "right")
},pause:function(A) {
    if ($chk(A)) {
        this.paused = (A) ? false : true
    }
    if (this.paused) {
        this.paused = false;
        this.delay = this.transition = 0;
        this.timer = this._preload.delay(100, this);
        [this.a,this.b].each(function(B) {
            ["morph","tween"].each(function(C) {
                if (this.retrieve(C)) {
                    this.get(C).resume()
                }
            }, B)
        });
        if (this.options.controller) {
            this.slideshow.getElement("." + this.classes.pause).removeClass(this.classes.play)
        }
    } else {
        this.paused = true;
        this.delay = Number.MAX_VALUE;
        this.transition = 0;
        $clear(this.timer);
        [this.a,this.b].each(function(B) {
            ["morph","tween"].each(function(C) {
                if (this.retrieve(C)) {
                    this.get(C).pause()
                }
            }, B)
        });
        if (this.options.controller) {
            this.slideshow.getElement("." + this.classes.pause).addClass(this.classes.play)
        }
    }
},next:function(A) {
    var B = (A) ? this.data.images.length - 1 : this.slide;
    this.go(B, "left")
},last:function() {
    this.next(true)
},load:function(C) {
    this.firstrun = true;
    this.showed = {array:[],i:0};
    if ($type(C) == "array") {
        this.options.captions = false;
        C = new Array(C.length).associate(C.map(function(H, G) {
            return H + "?" + G
        }))
    }
    this.data = {images:[],captions:[],hrefs:[],thumbnails:[]};
    for (var E in C) {
        var D = C[E] || {};
        var B = (D.caption) ? D.caption.trim() : "";
        var A = (D.href) ? D.href.trim() : ((this.options.linked) ? this.options.hu + E : this.options.href);
        var F = (D.thumbnail) ? D.thumbnail.trim() : E.replace(this.options.replace[0], this.options.replace[1]);
        this.data.images.push(E);
        this.data.captions.push(B);
        this.data.hrefs.push(A);
        this.data.thumbnails.push(F)
    }
    if (this.options.random) {
        this.slide = $random(0, this.data.images.length - 1)
    }
    if (this.options.thumbnails && this.slideshow.retrieve("thumbnails")) {
        this._thumbnails()
    }
    if (this.slideshow.retrieve("images")) {
        [this.a,this.b].each(function(G) {
            ["morph","tween"].each(function(H) {
                if (this.retrieve(H)) {
                    this.get(H).cancel()
                }
            }, G)
        });
        this.slide = this.transition = 0;
        this.go(0)
    }
    return this.data.images.length
},destroy:function(A) {
    this.events.each(function(C, B) {
        C.each(function(D) {
            document.removeEvent(B, D)
        })
    });
    this.pause(1);
    if (this.options.loader) {
        $clear(this.slideshow.retrieve("loader").retrieve("timer"))
    }
    if (this.options.thumbnails) {
        $clear(this.slideshow.retrieve("thumbnails").retrieve("timer"))
    }
    this.slideshow.uid = Native.UID++;
    if (A) {
        this.slideshow[A]()
    }
},_preload:function(A) {
    if (!this.preloader) {
        this.preloader = new Asset.image(this.options.hu + this.data.images[this.slide], {onload:function() {
            this.store("loaded", true)
        }})
    }
    if (this.preloader.retrieve("loaded") && $time() > this.delay && $time() > this.transition) {
        if (this.stopped) {
            if (this.options.captions) {
                this.slideshow.retrieve("captions").get("morph").cancel().start(this.classes.get("captions", "hidden"))
            }
            this.pause(1);
            if (this.end) {
                this.fireEvent("end")
            }
            this.stopped = this.end = false;
            return
        }
        this.image = (this.counter % 2) ? this.b : this.a;
        this.image.set("styles", {display:"block",height:"auto",visibility:"hidden",width:"auto",zIndex:this.counter});
        ["src","height","width"].each(function(D) {
            this.image.set(D, this.preloader.get(D))
        }, this);
        this._resize(this.image);
        this._center(this.image);
        var B = this.image.getParent();
        if (this.data.hrefs[this.slide]) {
            B.set("href", this.data.hrefs[this.slide])
        } else {
            B.erase("href")
        }
        var C = (this.data.captions[this.slide]) ? this.data.captions[this.slide].replace(/<.+?>/gm, "").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "'") : "";
        this.image.set("alt", C);
        if (this.options.titles) {
            B.set("title", C)
        }
        if (this.options.loader) {
            this.slideshow.retrieve("loader").fireEvent("hide")
        }
        if (this.options.captions) {
            this.slideshow.retrieve("captions").fireEvent("update", A)
        }
        if (this.options.thumbnails) {
            this.slideshow.retrieve("thumbnails").fireEvent("update", A)
        }
        this._show(A);
        this._loaded()
    } else {
        if ($time() > this.delay && this.options.loader) {
            this.slideshow.retrieve("loader").fireEvent("show")
        }
        this.timer = (this.paused && this.preloader.retrieve("loaded")) ? null : this._preload.delay(100, this, A)
    }
},_show:function(B) {
    if (!this.image.retrieve("morph")) {
        var C = (this.options.overlap) ? {duration:this.options.duration,link:"cancel"} : {duration:this.options.duration / 2,link:"chain"};
        $$(this.a, this.b).set("morph", $merge(C, {onStart:this._start.bind(this),onComplete:this._complete.bind(this),transition:this.options.transition}))
    }
    var E = this.classes.get("images", ((this.direction == "left") ? "next" : "prev"));
    var F = this.classes.get("images", "visible");
    var A = (this.counter % 2) ? this.a : this.b;
    if (B) {
        A.get("morph").cancel().set(E);
        this.image.get("morph").cancel().set(F)
    } else {
        if (this.options.overlap) {
            A.get("morph").set(F);
            this.image.get("morph").set(E).start(F)
        } else {
            var D = function(G, H) {
                this.image.get("morph").set(G).start(H)
            }.pass([E,F], this);
            E = this.classes.get("images", ((this.direction == "left") ? "prev" : "next"));
            A.get("morph").set(F).start(E).chain(D)
        }
    }
},_loaded:function() {
    this.counter++;
    this.delay = (this.paused) ? Number.MAX_VALUE : $time() + this.options.duration + this.options.delay;
    this.direction = "left";
    this.transition = (this.options.fast == 2 || (this.options.fast == 1 && this.paused)) ? 0 : $time() + this.options.duration;
    if (this.slide + 1 == this.data.images.length && !this.options.loop && !this.options.random) {
        this.stopped = this.end = true
    }
    if (this.options.random) {
        this.showed.i++;
        if (this.showed.i >= this.showed.array.length) {
            var A = this.slide;
            if (this.showed.array.getLast() != A) {
                this.showed.array.push(A)
            }
            while (this.slide == A) {
                this.slide = $random(0, this.data.images.length - 1)
            }
        } else {
            this.slide = this.showed.array[this.showed.i]
        }
    } else {
        this.slide = (this.slide + 1) % this.data.images.length
    }
    if (this.image.getStyle("visibility") != "visible") {
        (function() {
            this.image.setStyle("visibility", "visible")
        }).delay(1, this)
    }
    if (this.preloader) {
        this.preloader = this.preloader.destroy()
    }
    this._preload()
},_center:function(A) {
    if (this.options.center) {
        var B = A.getSize();
        A.set("styles", {left:(B.x - this.width) / -2,top:(B.y - this.height) / -2})
    }
},_resize:function(B) {
    if (this.options.resize) {
        var D = this.preloader.get("height"),A = this.preloader.get("width");
        var E = this.height / D,C = this.width / A,F;
        if (this.options.resize == "length") {
            F = (E > C) ? C : E
        } else {
            F = (E > C) ? E : C
        }
        B.set("styles", {height:Math.ceil(D * F),width:Math.ceil(A * F)})
    }
},_start:function() {
    this.fireEvent("start")
},_complete:function() {
    if (this.firstrun && this.options.paused) {
        this.firstrun = false;
        this.pause(1)
    }
    this.fireEvent("complete")
},_captions:function() {
    if (this.options.captions === true) {
        this.options.captions = {}
    }
    var B = this.slideshow.getElement(this.classes.get("captions"));
    var A = (B) ? B.empty() : new Element("div", {"class":this.classes.get("captions").substr(1)}).inject(this.slideshow);
    A.set({events:{update:function(D) {
        var C = this.slideshow.retrieve("captions");
        var F = (this.data.captions[this.slide] === "");
        if (D) {
            var G = (F) ? "hidden" : "visible";
            C.set("html", this.data.captions[this.slide]).get("morph").cancel().set(this.classes.get("captions", G))
        } else {
            var E = (F) ? $empty : function(H) {
                this.slideshow.retrieve("captions").set("html", this.data.captions[H]).morph(this.classes.get("captions", "visible"))
            }.pass(this.slide, this);
            C.get("morph").cancel().start(this.classes.get("captions", "hidden")).chain(E)
        }
    }.bind(this)},morph:$merge(this.options.captions, {link:"chain"})});
    this.slideshow.store("captions", A)
},_controller:function() {
    if (this.options.controller === true) {
        this.options.controller = {}
    }
    var E = this.slideshow.getElement(this.classes.get("controller"));
    var A = (E) ? E.empty() : new Element("div", {"class":this.classes.get("controller").substr(1)}).inject(this.slideshow);
    var C = new Element("ul").inject(A);
    $H({first:"Shift + Leftwards Arrow",prev:"Leftwards Arrow",pause:"P",next:"Rightwards Arrow",last:"Shift + Rightwards Arrow"}).each(function(J, I) {
        var G = new Element("li", {"class":(I == "pause" && this.options.paused) ? this.classes.play + " " + this.classes[I] : this.classes[I]}).inject(C);
        var H = this.slideshow.retrieve(I, new Element("a", {title:((I == "pause") ? this.classes.play.capitalize() + " / " : "") + this.classes[I].capitalize() + " [" + J + "]"}).inject(G));
        H.set("events", {click:function(K) {
            this[K]()
        }.pass(I, this),mouseenter:function(K) {
            this.addClass(K)
        }.pass(this.classes.active, H),mouseleave:function(K) {
            this.removeClass(K)
        }.pass(this.classes.active, H)})
    }, this);
    A.set({events:{hide:function(G) {
        if (!this.retrieve("hidden")) {
            this.store("hidden", true).morph(G)
        }
    }.pass(this.classes.get("controller", "hidden"), A),show:function(G) {
        if (this.retrieve("hidden")) {
            this.store("hidden", false).morph(G)
        }
    }.pass(this.classes.get("controller", "visible"), A)},morph:$merge(this.options.controller, {link:"cancel"})}).store("hidden", false);
    var B = function(H) {
        if (["left","right","p"].contains(H.key)) {
            var G = this.slideshow.retrieve("controller");
            if (G.retrieve("hidden")) {
                G.get("morph").set(this.classes.get("controller", "visible"))
            }
            switch (H.key) {case"left":this.slideshow.retrieve((H.shift) ? "first" : "prev").fireEvent("mouseenter");break;case"right":this.slideshow.retrieve((H.shift) ? "last" : "next").fireEvent("mouseenter");break;default:this.slideshow.retrieve("pause").fireEvent("mouseenter");break
            }
        }
    }.bind(this);
    this.events.keydown.push(B);
    var F = function(H) {
        if (["left","right","p"].contains(H.key)) {
            var G = this.slideshow.retrieve("controller");
            if (G.retrieve("hidden")) {
                G.store("hidden", false).fireEvent("hide")
            }
            switch (H.key) {case"left":this.slideshow.retrieve((H.shift) ? "first" : "prev").fireEvent("mouseleave");break;case"right":this.slideshow.retrieve((H.shift) ? "last" : "next").fireEvent("mouseleave");break;default:this.slideshow.retrieve("pause").fireEvent("mouseleave");break
            }
        }
    }.bind(this);
    this.events.keyup.push(F);
    var D = function(H) {
        var G = this.slideshow.retrieve("images").getCoordinates();
        if (H.page.x > G.left && H.page.x < G.right && H.page.y > G.top && H.page.y < G.bottom) {
            this.slideshow.retrieve("controller").fireEvent("show")
        } else {
            this.slideshow.retrieve("controller").fireEvent("hide")
        }
    }.bind(this);
    this.events.mousemove.push(D);
    document.addEvents({keydown:B,keyup:F,mousemove:D});
    this.slideshow.retrieve("controller", A).fireEvent("hide")
},_loader:function() {
    if (this.options.loader === true) {
        this.options.loader = {}
    }
    var A = new Element("div", {"class":this.classes.get("loader").substr(1),morph:$merge(this.options.loader, {link:"cancel"})}).store("hidden", false).store("i", 1).inject(this.slideshow.retrieve("images"));
    if (this.options.loader.animate) {
        for (var B = 0; B < this.options.loader.animate[1]; B++) {
            img = new Asset.image(this.options.loader.animate[0].replace(/#/, B))
        }
        if (Browser.Engine.trident4 && this.options.loader.animate[0].contains("png")) {
            A.setStyle("backgroundImage", "none")
        }
    }
    A.set("events", {animate:function() {
        var C = this.slideshow.retrieve("loader");
        var E = (C.retrieve("i").toInt() + 1) % this.options.loader.animate[1];
        C.store("i", E);
        var D = this.options.loader.animate[0].replace(/#/, E);
        if (Browser.Engine.trident4 && this.options.loader.animate[0].contains("png")) {
            C.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + D + '", sizingMethod="scale")'
        } else {
            C.setStyle("backgroundImage", "url(" + D + ")")
        }
    }.bind(this),hide:function() {
        var C = this.slideshow.retrieve("loader");
        if (!C.retrieve("hidden")) {
            C.store("hidden", true).morph(this.classes.get("loader", "hidden"));
            if (this.options.loader.animate) {
                $clear(C.retrieve("timer"))
            }
        }
    }.bind(this),show:function() {
        var C = this.slideshow.retrieve("loader");
        if (C.retrieve("hidden")) {
            C.store("hidden", false).morph(this.classes.get("loader", "visible"));
            if (this.options.loader.animate) {
                C.store("timer", function() {
                    this.fireEvent("animate")
                }.periodical(50, C))
            }
        }
    }.bind(this)});
    this.slideshow.retrieve("loader", A).fireEvent("hide")
},_thumbnails:function() {
    if (this.options.thumbnails === true) {
        this.options.thumbnails = {}
    }
    var C = this.slideshow.getElement(this.classes.get("thumbnails"));
    var E = (C) ? C.empty() : new Element("div", {"class":this.classes.get("thumbnails").substr(1)}).inject(this.slideshow);
    E.setStyle("overflow", "hidden");
    var A = new Element("ul", {tween:{link:"cancel"}}).inject(E);
    this.data.thumbnails.each(function(J, I) {
        var F = new Element("li").inject(A);
        var G = new Element("a", {events:{click:function(K) {
            this.go(K);
            return false
        }.pass(I, this),loaded:function() {
            this.data.thumbnails.pop();
            if (!this.data.thumbnails.length) {
                var O = E.getCoordinates();
                var M = E.retrieve("props");
                var K = 0,N = M[1],L = M[2];
                E.getElements("li").each(function(P) {
                    var P = P.getCoordinates();
                    if (P[N] > K) {
                        K = P[N]
                    }
                }, this);
                E.store("limit", O[L] + O[M[0]] - K)
            }
        }.bind(this)},href:this.options.hu + this.data.images[I],morph:$merge(this.options.thumbnails, {link:"cancel"})}).inject(F);
        if (this.data.captions[I] && this.options.titles) {
            G.set("title", this.data.captions[I].replace(/<.+?>/gm, "").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "'"))
        }
        var H = new Asset.image(this.options.hu + J, {onload:function() {
            this.fireEvent("loaded")
        }.bind(G)}).inject(G)
    }, this);
    E.set("events", {scroll:function(H, K) {
        var F = this.getCoordinates();
        var L = this.getElement("ul").getPosition();
        var N = this.retrieve("props");
        var I = N[3],R,O = N[0],T = N[2],P;
        var S = this.getElement("ul").get("tween", {property:O});
        if ($chk(H)) {
            var Q = this.getElements("li")[H].getCoordinates();
            R = F[O] + (F[T] / 2) - (Q[T] / 2) - Q[O];
            P = (L[I] - F[O] + R).limit(this.retrieve("limit"), 0);
            if (K) {
                S.set(P)
            } else {
                S.start(P)
            }
        } else {
            var G = F[N[2]] / 3,M = this.retrieve("page"),J = -0.2;
            if (M[I] < (F[O] + G)) {
                R = (M[I] - F[O] - G) * J
            } else {
                if (M[I] > (F[O] + F[T] - G)) {
                    R = (M[I] - F[O] - F[T] + G) * J
                }
            }
            if (R) {
                P = (L[I] - F[O] + R).limit(this.retrieve("limit"), 0);
                S.set(P)
            }
        }
    }.bind(E),update:function(F) {
        var G = this.slideshow.retrieve("thumbnails");
        G.getElements("a").each(function(H, I) {
            if (I == this.slide) {
                if (!H.retrieve("active", false)) {
                    H.store("active", true);
                    var K = this.classes.get("thumbnails", "active");
                    if (F) {
                        H.get("morph").set(K)
                    } else {
                        H.morph(K)
                    }
                }
            } else {
                if (H.retrieve("active", true)) {
                    H.store("active", false);
                    var J = this.classes.get("thumbnails", "inactive");
                    if (F) {
                        H.get("morph").set(J)
                    } else {
                        H.morph(J)
                    }
                }
            }
        }, this);
        if (!G.retrieve("mouseover")) {
            G.fireEvent("scroll", [this.slide,F])
        }
    }.bind(this)});
    var D = E.getCoordinates();
    E.store("props", (D.height > D.width) ? ["top","bottom","height","y"] : ["left","right","width","x"]);
    var B = function(F) {
        var G = this.getCoordinates();
        if (F.page.x > G.left && F.page.x < G.right && F.page.y > G.top && F.page.y < G.bottom) {
            this.store("page", F.page);
            if (!this.retrieve("mouseover")) {
                this.store("mouseover", true);
                this.store("timer", function() {
                    this.fireEvent("scroll")
                }.periodical(50, this))
            }
        } else {
            if (this.retrieve("mouseover")) {
                this.store("mouseover", false);
                $clear(this.retrieve("timer"))
            }
        }
    }.bind(E);
    this.events.mousemove.push(B);
    document.addEvent("mousemove", B);
    this.slideshow.store("thumbnails", E)
}});