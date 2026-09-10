const triggerBtn = document.getElementById('waTriggerBtn');
const popupBox = document.getElementById('waPopupBox');
const closeBtn = document.getElementById('waCloseBtn');
const waIcon = document.getElementById('waIcon');

triggerBtn.addEventListener('click', () => {
    if (popupBox.style.display === 'block') {
        closePopup();
    } else {
        openPopup();
    }
});

closeBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    closePopup();
});

function openPopup() {
    popupBox.style.display = 'block';
    waIcon.className = 'ri-close-line';
}

function closePopup() {
    popupBox.style.display = 'none';
    waIcon.className = 'ri-chat-3-line';
}