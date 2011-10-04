/**
 * Created by PyCharm.
 * User: jon
 * Date: 10/3/11
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */


var lastPage = 1;

function showPage(pageNumber, pages) {

    $(".page_"+lastPage).fadeOut('fast');
    $(".page_"+lastPage+"_link").css({
        'text-decoration':'underline',
        'color':'#B84820'
    });


    $(".page_"+pageNumber).fadeIn('fast');
    $(".page_"+pageNumber+"_link").css({
        'text-decoration':'none',
        'color':'inherit'
    });

    lastPage = pageNumber;
}