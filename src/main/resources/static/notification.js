function showNotification(message) {
    if ($("#notification").length === 0) {
        $("body").append(`
      <div id="notification" class="alert alert-success alert-dismissible fade show" role="alert">
        <strong style="font-size: 34px" id="notification-message"></strong>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
    `);

        // Thêm  
        $("head").append(`
      <style>
        #notification {
          position: fixed;
          top: 20px;
          right: 20px;
          z-index: 1050;
          display: none; 
        }
      </style>
    `);
    }

    $("#notification-message").html(message);
    $("#notification").fadeIn();

    setTimeout(function() {
        $("#notification").fadeOut();
    }, 2000);
}