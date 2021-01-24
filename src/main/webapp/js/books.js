let pageNum = 1;
$('.page-num').text(pageNum);

// $(function() { // on page load
//     $('select').on("change", function() {
//         let sortAttr = $('option:selected', this).attr('id');
//         $.get('http://localhost:8080/sort?sort=' +
//             sortAttr, function(responseText) {
//             $("#mydiv").text(responseText);           // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
//         });
//         // $('#mydiv').load('http://localhost:8080/sort?sort=' +
//         //     sortAttr +
//         //     ' #mydiv'); // copy myDiv from the result
//
//     });
// })

let books;

function fetchData(pageNumber) {
    if (pageNumber == null) {
        pageNumber = 1
    }

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({page: pageNumber}),
        success: function (jsonStr) {
            // books = JSON.parse(jsonStr);
            books = jsonStr;
            render(books);
            // $("#toR").load(" #toR");
            // $('#mydiv').load('http://localhost:8080/books' + ' #mydiv');
        }
    });
}

$(document).ready(function () {
    fetchData(1);
})


function render(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {
            if (el.base64Image != null && el.base64Image !== "") {
                let block = '<div class="col-4" style="flex-basis: 25%"><a href="/home?command=book_details&isbn=' + el.isbn + '">' +
                            '<img src="data:image/jpg;base64,' + el.base64Image + '">' +
                            '<h4>' + el.title + '</h4>' +
                            '<p>' + el.price + '&#36;</p>' +
                            '</a></div>';
                $('#toInsert').append(block);
            }
        })

        // let divs = $('#toInsert div');
        // for (let i = 0; i < divs.length; ++i) {
        //     $(divs[i]).attr('class', 'col-4');
        //     $(divs[i]).css('flex-basis', '25%');
        // }
    }

    if (books == null) {
        let block;
        let locale =  $('#locale').text();

        if (locale === 'US') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>Not found</h1>' +
                // '<h1><fmt:message key="not_found" bundle="${mes}"/></h1>' +
                '<br>' +
                // '<p><fmt:message key="no_books_with_genre" bundle="${mes}"/></p>' +
                '<p>No suitable book found</p>' +
                '<br>' +
                '<a href="/home" class="btn">Choose another genre</a>' +
                // '<a href="/home" class="btn"><fmt:message key="choose_another_genre" bundle="${m}"/></a>' +
                '</div>';
        }
        if (locale === 'RU') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>Не найдено</h1>' +
                '<br>' +
                '<p>Не найдено подходящей книги</p>' +
                '<br>' +
                '<a href="/home" class="btn">Выберите другой жанр</a>' +
                '</div>';
        }

        $('#toInsert').append(block);
    }
}

$('select').change(function () {
    $( "select option:selected" ).each(function() {
        let sortAttr = $('option:selected').attr('id');

        switch (sortAttr) {
            case 'price':
                books.sort((a,b) => (a.price > b.price) ? 1 : ((b.price > a.price) ? -1 : 0));
                console.log('price');
                break;
            case 'author':
                books.sort((a,b) => (a.author > b.author) ? 1 : ((b.author > a.author) ? -1 : 0));
                console.log('author');
                break;
            default:
                books.sort(() => Math.random() - 0.5);
                console.log('default');
                break;
        }

        render(books);

        // $.ajax({
        //     url: 'http://localhost:8080/sort',
        //     type: 'GET',
        //     data: ({sort: sortAttr}),
        //     success: function (jsonStr) {
        //         $('#mydiv').append('<p>' + jsonStr + '</p>>');
        //         // $("#toR").load(" #toR");
        //
        //
        //         // $('#mydiv').load('http://localhost:8080/books' + ' #mydiv');
        //         console.log(location.href)
        //     }
        // });
    })
});




$(document).ready(function () {
    $('.prev').bind('click', function () {
        pageNum--;
        $.ajax({
            url: 'http://localhost:8080/books',
            type: 'GET',
            data: ({page: pageNum}),
            success: function () {
                $('.small-container').load(' .small-container');
                $('.page-num').text(pageNum);
            }
        });
    });
    $('.next').bind('click', function () {
        pageNum++;
        $.ajax({
            url: 'http://localhost:8080/books',
            type: 'GET',
            data: ({page: pageNum}),
            success: function () {
                $('.small-container').load(' .small-container');
                $('.page-num').text(pageNum);
            }
        });
    });

    // $('select').change(function () {
    //     $( "select option:selected" ).each(function() {
    //         let sortAttr = $('option:selected').attr('id');
    //         console.log(sortAttr);
    //         $.ajax({
    //             url: 'http://localhost:8080/sort',
    //             type: 'GET',
    //             data: ({sort: sortAttr}),
    //             success: function () {
    //                 // $("#mydiv").load(location.href+" #mydiv>*","");
    //                 // $('#mydiv').load(location.href + ' #mydiv');
    //                 // $("#mydiv").load(" #mydiv");
    //                 $("#toR").load(" #toR");
    //
    //
    //                 // $('#mydiv').load('http://localhost:8080/books' + ' #mydiv');
    //                 console.log(location.href)
    //             }
    //         });
    //     })
    // });
})