var BACKEND_PATH = "http://localhost:8080";

function loadMenu()
{
    $.ajax({
        url: `${BACKEND_PATH}/menu`,
        method: 'get',
        dataType: 'json',
        headers: {
            "Connection":"keep-alive",
            // "Cookie":"322576F2C7854C0F2FA29C4C27069D4C"
        },
        success: function(data){
            console.log(data);
            fillMenu(data);
        },
        error: function (jqXHR, exception) {
            if (jqXHR.status === 0) {
                alert('Not connect. Verify Network.');
            } else if (jqXHR.status == 404) {
                alert('Requested page not found (404).');
            } else if (jqXHR.status == 500) {
                alert('Internal Server Error (500).');
            } else if (exception === 'parsererror') {
                alert('Requested JSON parse failed.');
            } else if (exception === 'timeout') {
                alert('Time out error.');
            } else if (exception === 'abort') {
                alert('Ajax request aborted.');
            } else {
                alert('Uncaught Error. ' + jqXHR.responseText);
            }
        }
    });
}
function fillMenu(menu)
{
    menu.forEach(item => {
        console.log(item);
        document.getElementById("main").insertAdjacentHTML(
            `beforeend`,
            `<div style="width: 200px; margin: 25px; display: flex; flex-direction: column; justify-content: flex-start;">
                    <p style="font-size: 25px; height: 60px;">${item.title}<p>
                    <p style="font-size: 15px; height: 60px;">${item.description}<p>
                    
                    ${item.available_variants.length == 0 ? "<p style='height: 23px'></p>" : `<div style="display: flex;"><label for=\"variants\">${item.variants_description}</label>\n <select name=\"variants\" id=\"variants${item.id}\"  style="color: black"></select></div>`}
                            
                    <p style="font-size: 20px">${item.price} BYN</p>
                    <input type="number" id="quantity" name="quantity" min="1" max="5">
                    <button style="color: black">Add To Order</button>
                </div>`
        );
        item.available_variants.forEach(variant => {
            document.getElementById(`variants${item.id}`).insertAdjacentHTML(
                `beforeend`,
                `<option style="color: white" value="${variant}">${variant.name}   ${variant.additionalPrice != 0 ? "+"+variant.additionalPrice+"BYN" : ""}</option>`
            );
        });
    });
}

loadMenu();