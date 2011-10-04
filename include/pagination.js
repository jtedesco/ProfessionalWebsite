/**
 * Created by PyCharm.
 * User: jon
 * Date: 10/3/11
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */

function showPage(pageNumber, pages) {
    for(var i=0; i<=pages; i++) {
        $(".page_"+i).hide();
        $(".page_"+i+"_link").css({
            'text-decoration':'underline',
            'color':'#B84820'
        });
    }
    $(".page_"+pageNumber).show();
    $(".page_"+pageNumber+"_link").css({
            'text-decoration':'none',
            'color':'inherit'
        });
}