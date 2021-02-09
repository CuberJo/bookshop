
let relatedBooks;

/**
 * Fetches bestsellers from server
 */
function fetchRelatedBooks() {

    $.ajax({
        // url: 'http://localhost:8080/books',
        url: 'http://localhost:8080/home',
        type: 'GET',
        data: ({
            // relatedBooks: 'fetch'
            command: 'relatedBooks'
        }),
        success: function (jsonStr) {
            relatedBooks = jsonStr;
            console.log('relatedBooks: ' + relatedBooks);
            renderRelatedBooks(relatedBooks);
        }
    });
}


/**
 * When fetches data from server when document is ready
 */
$(document).ready(function () {
    fetchRelatedBooks();
})









/**
 * Renders JSON string on page
 * @param books
 */
function renderRelatedBooks(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {
            if (el.base64Image != null && el.base64Image !== "") {
                let block = '<div class="col-4"><a href="/home?command=book_details&isbn=' + el.isbn + '">' +
                    '<img height="" src="data:image/jpg;base64,' + el.base64Image + '">' +
                    '<h4>' + el.title + '</h4>' +
                    '<div class="rating">' +
                        '<i class="fa fa-star"></i>' +
                        '<i class="fa fa-star"></i>' +
                        '<i class="fa fa-star"></i>' +
                        '<i class="fa fa-star"></i>' +
                        '<i class="fa fa-star-o"></i>' +
                    '</div>' +
                    '<p>' + el.price + '&#36;</p>' +
                    '</a></div>';

                $('#relatedBooks').append(block);
            }
        })

        // let divs = $('#toInsert div');
        // for (let i = 0; i < divs.length; ++i) {
        //     $(divs[i]).attr('class', 'col-4');
        //     $(divs[i]).css('flex-basis', '25%');
        // }
    }

    if (books == null || books.length === 0) {
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
                '<h1>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e</h1>' +
                '<br>' +
                '<p>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0435\u0439 \u043a\u043d\u0438\u0433\u0438</p>' +
                '<br>' +
                '<a href="/home" class="btn">\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0434\u0440\u0443\u0433\u043e\u0439 \u0436\u0430\u043d\u0440</a>' +
                '</div>';
        }

        $('#relatedBooks').append(block);
    }
}
