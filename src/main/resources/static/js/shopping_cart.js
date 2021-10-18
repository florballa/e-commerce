$(document).ready(function(){

    $(".minusButton").on("click", function(evt){
            evt.preventDefault();
            decreaseQuantity($(this));
    });

    $(".plusButton").on("click", function(evt){
             evt.preventDefault();
             increaseQuantity($(this));
    });

    $(".link-remove").on("click", function(evt){
        evt.preventDefault();
        removeFromCart($(this));
    });

    updateTotal();
});

function removeFromCart(link){
    url = link.attr("href");

    $.ajax({
        type: "POST",
        url: url,
    }).done(function(response){
        if(response.includes("removed")){
          rowNumber = link.attr("rowNumber");
          removeProduct(rowNumber);
          updateTotal();
        }
    });
}

function removeProduct(rowNumber){
    rowId = "row" + rowNumber;
    $("#" + rowId).remove();
}

function decreaseQuantity(link){
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);
    newQty = parseInt(qtyInput.val()) - 1;
    if(newQty > 0) {
        qtyInput.val(newQty);
        updateQuantity(productId, newQty);
    }
}

function increaseQuantity(link){
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);
    newQty = parseInt(qtyInput.val()) + 1;
    if(newQty < 10) {
        qtyInput.val(newQty);
        updateQuantity(productId, newQty);
    }
}

function updateQuantity(productId, quantity){
    url = contextPath + "shopping/cart/update/" + productId + "/" + quantity;

    $.ajax({
            type: "POST",
            url: url,
        }).done(function(newSubtotal){
            updateSubtotal(newSubtotal, productId);
            updateTotal();
        });
}

function updateSubtotal(newSubtotal, productId){
    $("#subtotal" + productId).text(newSubtotal);
}

function updateTotal(){
    total = 0.0;

    $(".productSubtotal").each(function(index, element){
        total = total + parseFloat(element.innerHTML);
    });

    $("#totalAmount").text("$" + total);
}