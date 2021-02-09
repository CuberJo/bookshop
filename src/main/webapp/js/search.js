
let searchCriteria;
let typingTimer;                //timer identifier
let doneTypingInterval = 1500;  //time in ms (1,5 seconds)
let searchStr;
const STR_LEN = 1

$(document).ready(function(e) {
    $('.search-panel .dropdown-menu').find('a').click(function(e) {
        e.preventDefault();
        let param = $(this).attr("href").replace("#", "");
        let concept = $(this).text();

        searchCriteria = this.className;
        $('#scr').val(searchCriteria);

        $('.search-panel span#search_concept').text(concept);
        $('.input-group #search_param').val(param);
    });
    // $('ul li a').click(function(){
    //     chosenSearchCriteria = $(this).attr('id');
    // });
    // $('#srch').click(function (e) {
    //     // e.preventDefault();
    //     if (!validateSearchInput(e)) {
    //         return;
    //     }
    // })
});


$(function () {
    $('#searchBtn').click(function (event) {
        if (!validateSearchInput(event)) {
            event.preventDefault();
        }
    });
});



/**
 * Calls another fun to fetch data from server
 * after some period if time and renders received data
 *
 * @param str
 */
function loadBooks(str) {

    $('#errorSearchMessage').css('display', 'none');

    // if (!validateSearchInput(event, str)) {
    //     return;
    // }

    /**
     * Clear search result hint
     */
    const whitespace_regex = /^[\s]+$/;

    if (str.length <= STR_LEN || whitespace_regex.test(str)) {
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
}


/**
 * Function responsible for fetching data from server
 */
function load() {

    if (!validateSearchInput()) {
        return;
    }

    /**
     * Instantiating XMLHttpRequest object
     *
     * @type {XMLHttpRequest}
     */
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {

            $('#livesearch').empty();

            if (this.response == "") {
                document.getElementById("livesearch").style.border="0px";
                return;
            }

            $.each(this.response, function(index, el) {
                console.log('item ' + el.title);

                $('#livesearch').append(
                    '<a href="/home?command=book_details&isbn=' + el.isbn + '"><div class="sr">' +
                    '<img width="48px" src="data:image/jpg;base64,' + el.base64Image + '">' +
                    '<div><h4>' + el.title + '</h4>' +
                    '<p>' + el.price + '&#36;</p></div>' +
                    '</div></a>'
                )
            });
            document.getElementById("livesearch").style.border = "1px solid #A5ACB2";
        }
    }


    /**
     * Preparing URL object and adding params
     */
    const SEARCH_CRITERIA_PARAM = 'searchCriteria';
    const STR_PARAM = 'str';

    // let url = new URL('http://localhost:8080/search_books');
    // let url = new URL('http://localhost:8080/books?command=live_book_search');
    let url = new URL('http://localhost:8080/home?command=live_book_search');
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


/**
 * Validates searh string
 *
 * @returns {boolean}
 */
function validateSearchInput(event) {

    searchStr = $('#search').val();
    console.log('searchStr ' + searchStr);

    let error;

    searchStr = searchStr.replace(/\s+$/, '');
    searchStr = searchStr.replace(/^\s+/, '');


    let locale =  $('#searchlocale').text();
    let malicious_regex = /[<>*;='#)+&("]+/;
    if (malicious_regex.test(searchStr)) {
        if(event !== undefined) {
            event.preventDefault();
        }
        if (locale === 'RU') {
            error = 'Неверно введены данные';
        } else {
            error = 'Invalid input data';
        }
        return false;
    }

    if (searchStr === "" || searchStr.length <= STR_LEN) {
        if(event !== undefined) {
            event.preventDefault();
        }
        return false;
    }

    // let whitespace_regex = /^[\s]+$/;
    // if (searchStr === "" || whitespace_regex.test(searchStr)) {
    //     event.preventDefault();
    //     if (locale === 'RU') {
    //         error = 'Неверно введены данные';
    //     } else {
    //         error = 'Invalid input data';
    //     }
    // }

    if (error !== "" && error !== undefined) {
        $('#errorSearchMessage').css('display', 'block');
        $("#errorSearchMessage").text(error);
        return false;
    }

    $('#str').val(searchStr)

    return true;
}

