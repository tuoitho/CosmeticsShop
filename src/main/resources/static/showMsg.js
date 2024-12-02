function showModalWithMessage(message) {
    // Update the message in the modal
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;

    // Get the modal element
    const modalElement = document.getElementById('customModal');

    // Create a new modal instance
    const modal = new bootstrap.Modal(modalElement);

    // Show the modal
    modal.show();
//     event nut close and ok
    const closeBtn = document.getElementById('closeButton');
    closeBtn.addEventListener('click', function() {
        modal.hide();
    });

    const okBtn = document.getElementById('okButton');
    okBtn.addEventListener('click', function() {
        modal.hide();
    });
}
