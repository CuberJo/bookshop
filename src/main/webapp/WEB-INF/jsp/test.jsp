<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Books - Bookstore</title>
    <%--    <link rel="stylesheet" type="text/css" href="../../styles/books.css">--%>
    <%--    <link rel="stylesheet" type="text/css" href="../../styles/home.css">--%>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <%--    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!--  -->
    <%--    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>--%>
    <!--  -->
</head>

<body>

<%--<jsp:include page="header.jsp" />--%>


<%--    <style>--%>
<%--        .row .row .col-4{--%>
<%--            float: left;--%>
<%--            width:25%;--%>
<%--            position: relative;--%>
<%--        }--%>
<%--        .row .row .col-4.hide{--%>
<%--            display: none;--%>
<%--        }--%>
<%--        .row .row .col-4.show{--%>
<%--            display: block;--%>
<%--            animation: show .5s ease;--%>
<%--        }--%>
<%--        .pagination{--%>
<%--            width: 100%;--%>
<%--            float: left;--%>
<%--            padding:15px;--%>
<%--            text-align: center;--%>
<%--        }--%>
<%--        .pagination div{--%>
<%--            display: inline-block;--%>
<%--            margin:0 10px;--%>
<%--        }--%>
<%--        .pagination .page{--%>
<%--            color:gray;--%>
<%--        }--%>
<%--        .pagination .prev,.gallery .pagination .next{--%>
<%--            color:#000;--%>
<%--            border:1px solid #000;--%>
<%--            font-size:15px;--%>
<%--            padding:7px 15px;--%>
<%--            cursor: pointer;--%>
<%--        }--%>

<%--        .pagination .prev.disabled,--%>
<%--        .pagination .next.disabled{--%>
<%--            border-color: gray;--%>
<%--            color:gray;--%>
<%--            pointer-events: none;--%>
<%--        }--%>
<%--    </style>--%>

<style>
    /*body{*/
    /*    margin:0;*/
    /*    font-family: sans-serif;*/
    /*    background-color: #efefef;*/
    /*}*/
    /**{*/
    /*    box-sizing: border-box;*/
    /*}*/

    /*.container{*/
    /*    max-width: 1000px;*/
    /*    margin: auto;*/
    /*}*/
    /*.gallery{*/
    /*    width: 100%;*/
    /*    float: left;*/
    /*    padding:30px 15px;*/
    /*}*/
    /*.gallery .title h1{*/
    /*    font-size:36px;*/
    /*    margin:0 0 30px;*/
    /*    color:#000000;*/
    /*    text-align: center;*/
    /*}*/
    /*.gallery .gallery-items .item{*/
    /*    float: left;*/
    /*    width:25%;*/
    /*    position: relative;*/
    /*}*/
    .container .gallery-items .item.hide{
        display: none;
    }
    .container .gallery-items .item.show{
        display: block;
        animation: show .5s ease;
    }
    /*@keyframes show{*/
    /*    0%{*/
    /*        opacity:0;*/
    /*        transform: scale(0.9);*/
    /*    }*/
    /*    100%{*/
    /*        opacity:1;*/
    /*        transform: scale(1);*/
    /*    }*/
    /*}*/


    /*.gallery .gallery-items .item img{*/
    /*    width: 100%;*/
    /*    display: block;*/
    /*}*/
    /*.gallery .gallery-items .item .caption{*/
    /*    position: absolute;*/
    /*    left:0px;*/
    /*    bottom:0;*/
    /*    background-color: rgba(0,0,0,.5);*/
    /*    padding:10px;*/
    /*    width: 100%;*/
    /*    color:#ffffff;*/
    /*    text-align: center;*/
    /*}*/
    .container .pagination{
        width: 100%;
        float: left;
        padding:15px;
        text-align: center;
    }
    .container .pagination div{
        display: inline-block;
        margin:0 10px;
    }
    .container .pagination .page{
        color:gray;
    }
    .container .pagination .prev,.container .pagination .next{
        color:#000;
        border:1px solid #000;
        font-size:15px;
        padding:7px 15px;
        cursor: pointer;
    }

    .container .pagination .prev.disabled,
    .container .pagination .next.disabled{
        border-color: gray;
        color:gray;
        pointer-events: none;
    }
</style>


<%--<section class="gallery">--%>
<div class="container">
    <%--        <div class="title">--%>
    <%--            <h1>Gallery</h1>--%>
    <%--        </div>--%>
    <div class="gallery-items">
        <c:forEach var="book" items="${requestScope.books}">
                <div class="item">
            <img src="../../images/library.jpg" width="100px" height="100px">
            <h4>${book.title}</h4>
            <p>${book.price}</p>
        </div>
        </c:forEach>

    </div>
    <div class="pagination">
        <div class="prev">Prev</div>
        <div class="page">Page <span class="page-num"></span></div>
        <div class="next">Next</div>
    </div>
</div>
<%--</section>--%>

<!---------- js for pagination --------------->

<script type="text/javascript">

    const galleryItems=document.querySelector(".gallery-items").children;
    const prev=document.querySelector(".prev");
    const next=document.querySelector(".next");
    const page=document.querySelector(".page-num");
    const maxItem=8;
    let index=1;


    const pagination=Math.ceil(galleryItems.length/maxItem);
    console.log(pagination)

    prev.addEventListener("click",function(){
        index--;
        check();
        showItems();
    })
    next.addEventListener("click",function(){
        index++;
        check();
        showItems();
    })

    function check(){
        if(index==pagination){
            next.classList.add("disabled");
        }
        else{
            next.classList.remove("disabled");
        }

        if(index==1){
            prev.classList.add("disabled");
        }
        else{
            prev.classList.remove("disabled");
        }
    }

    function showItems() {
        for(let i=0;i<galleryItems.length; i++){
            galleryItems[i].classList.remove("show");
            galleryItems[i].classList.add("hide");


            if(i>=(index*maxItem)-maxItem && i<index*maxItem){
                // if i greater than and equal to (index*maxItem)-maxItem;
                // means  (1*8)-8=0 if index=2 then (2*8)-8=8
                galleryItems[i].classList.remove("hide");
                galleryItems[i].classList.add("show");
            }
            page.innerHTML=index;
        }


    }

    window.onload=function(){
        showItems();
        check();
    }
</script>

</body>
</html>