/**
 * Created by PyCharm.
 * User: jon
 * Date: 10/3/11
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */


var lastPage = 1;

function showPage(pageNumber, numberOfPages, firstPage) {

    if(firstPage) {

        $(".page_"+pageNumber).show();
        $(".page_"+pageNumber+"_link").css({
            'text-decoration':'none',
            'color':'inherit'
        });


    } else {

        if(pageNumber != lastPage) {

            $(".page_"+lastPage).fadeOut('fast');

            $(".page_"+lastPage+"_link").css({
                'text-decoration':'underline',
                'color':'#B84820'
            });

            $(".page_"+pageNumber+"_link").css({
                'text-decoration':'none',
                'color':'inherit'
            });
            $(".page_"+pageNumber).fadeIn('fast');

        }
    }

    // Hide all other pages
    for(var i = 1; i<numberOfPages+1; i++) {
        if(i != pageNumber) {
            hidePage(i);
        }
    }

    lastPage = pageNumber;
}

function hidePage(pageNumber) {
    $(".page_"+pageNumber).hide();
}