
let searchCriteria;
let typingTimer;                //timer identifier
let doneTypingInterval = 1500;  //time in ms (5 seconds)
let searchStr;

$(document).ready(function(e) {
    $('.search-panel .dropdown-menu').find('a').click(function(e) {
        e.preventDefault();
        let param = $(this).attr("href").replace("#", "");
        let concept = $(this).text();

        searchCriteria = this.id;

        $('.search-panel span#search_concept').text(concept);
        $('.input-group #search_param').val(param);
    });
});
// var a = document.getElementsByTagName('a').item(0);
// console.log(a);
// $(function(){ // this will be called when the DOM is ready
//     var t = $('a');
//     console.log(t)
//     t.item(0).onclick(function() {
//         console.log('hi')
//         console.log($('a')[0]);
//         alert('Handler for .keyup() called.');
//     });
// });


/**
 * Calls another fun to fetch data from server
 * after some period if time and renders received data
 *
 * @param str
 */
function loadBooks(str) {
    /**
     * Clear search result hint
     */
    if (str.length === 0) {
        document.getElementById("livesearch").innerHTML="";
        document.getElementById("livesearch").style.border="0px";
        return;
    }

    searchStr = str;

    /**
     * Waits till user stops typing
     */
    clearTimeout(typingTimer);
    typingTimer = setTimeout(load, doneTypingInterval);


    // $.ajax({
    //     url: 'http://localhost:8080/loadBooks',
    //     type: 'GET',
    //     data: ({page: ++pageNum, genre: $('#genre').text()}),
    //     success: function (jsonStr) {
    //         books = jsonStr;
    //         $('#toInsert').empty();
    //         render(books);
    //         $('.page-num').text(' ' + pageNum);
    //         if (pageNum > 0) {
    //             showPrev();
    //         }
    //         if (pageNum * booksPerPage >= booksQuantity) {
    //             hideNext();
    //         }
    //     }
    // });
}


/**
 * Function responsible for fetching data from server
 */
function load() {
    /**
     * Instantiating XMLHttpRequest object
     *
     * @type {XMLHttpRequest}
     */
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById("livesearch").innerHTML = this.response;
            document.getElementById("livesearch").style.border = "1px solid #A5ACB2";

            let responseObj = xhr.response;
            alert(responseObj.title);
        }
    }

    проверить что здесь
    // xhr.onload = function() {
    //     let responseObj = xhr.response;
    //     alert(responseObj.title); // Привет, мир!
    // };


    /**
     * Preparing URL object and adding params
     *
     * @type {string}
     */
    const SEARCH_CRITERIA_PARAM = 'searchCriteria';
    const STR_PARAM = 'str';

    let url = new URL('http://localhost:8080/search_books');
    url.searchParams.set(SEARCH_CRITERIA_PARAM, searchCriteria);
    url.searchParams.set(STR_PARAM, searchStr);


    /**
     * Sending request
     *
     * @type {string}
     */
    let async = true;
    xhr.open('GET', url, async); // xmlhttp.open("GET","livesearch.php?q="+str,true);
    xhr.responseType = 'json';
    xhr.send();
    searchStr = undefined;
}


// $(function () {
//     $('#srch').on('click', function () {
//         $.ajax({
//             url:
//         })
//     })
// })