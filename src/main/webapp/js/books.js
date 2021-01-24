
let pageNum = 1;
const booksPerPage = 8;
let booksQuantity;
$('.page-num').text(' ' + pageNum);

hidePrev();

let books;


/**
 * Fetches data from server
 * @param {number} pageNumber
 */
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
            // or
            // $('.small-container').load(' .small-container');
            // $('.page-num').text(pageNum);
        }
    });
}

почему все равно виден след prev элемент

/**
 * Fetches books quantity
 */
function fetchBooksQuantity() {
    let needCount = 1;

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({count: needCount}),
        success: function (rows) {
            booksQuantity = rows;
            console.log('rows: ' + rows);
            if (booksQuantity < booksPerPage) {
                hideNext();
            }
            fetchData(1)
        }
    });
}




/**
 * When fetches data from server when document is ready
 */
$(document).ready(function () {
    // fetchData(1);
    fetchBooksQuantity();
})





/**
 * Renders JSON string on page
 * @param books
 */
function render(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {
            if (el.base64Image != null && el.base64Image !== "") {
                let block = '<div class="col-4" style="flex-basis: 25%"><a href="/home?command=book_details&isbn=' + el.isbn + '">' +
                            '<img height="" src="data:image/jpg;base64,' + el.base64Image + '">' +
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





/**
 * Binding buttons with actions
 */
$(document).ready(function () {
    $('.prev').bind('click', function () {
        if (--pageNum > 0) {
            $.ajax({
                url: 'http://localhost:8080/books',
                type: 'GET',
                data: ({page: pageNum}),
                success: function (jsonStr) {
                    books = jsonStr;
                    $('#toInsert').empty();
                    render(books);
                    $('.page-num').text(' ' + pageNum);

                    if ((pageNum * booksPerPage + 1) < booksQuantity) {
                        showNext();
                    }
                }
            });
        } else {
            pageNum++;
            hidePrev();
        }
    });
    $('.next').bind('click', function () {
        if ((pageNum * booksPerPage + 1) < booksQuantity) {
            $.ajax({
                url: 'http://localhost:8080/books',
                type: 'GET',
                data: ({page: ++pageNum}),
                success: function (jsonStr) {
                    books = jsonStr;
                    $('#toInsert').empty();
                    render(books);
                    $('.page-num').text(' ' + pageNum);
                    if (pageNum > 0) {
                        showPrev();
                    }
                }
            });
        } else {
            hideNext();
        }
    });

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

            $('#toInsert').empty();
            render(books);
        })
    });
})




function hideNext() {
    $('.next').css('border-color', 'gray');
    $('.next').css('color', 'gray');
    $('.next').css('pointer-events', 'none');
}

function showNext() {
    $('.next').css('color', '#000');
    $('.next').css('border-color', '#000');
    $('.next').css('cursor', 'pointer');
}

function hidePrev() {
    $('.prev').css('border-color', 'gray');
    $('.prev').css('color', 'gray');
    $('.prev').css('pointer-events', 'none');
}

function showPrev() {
    $('.prev').css('color', '#000');
    $('.prev').css('border-color', '#000');
    $('.prev').css('cursor', 'pointer');
}