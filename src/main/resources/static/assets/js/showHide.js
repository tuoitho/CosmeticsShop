const $2 = document.querySelectorAll.bind(document)
const $1 = document.querySelector.bind(document)

//modal show hide search
const iconSearch = $1('.search-link')
const modal =  $1('.modal')
const iconClose = $1('.modal__icon-close')
const inputSearch = $1('.modal__input')
const searchHistory = $1('.modal__history')

function showSearch () {
    modal.classList.add('modal__open')
}
function hideSearch () {
    modal.classList.remove('modal__open')
}

iconSearch.addEventListener('click', showSearch)
iconClose.addEventListener('click', hideSearch)
modal.addEventListener('click',hideSearch)
inputSearch.addEventListener('click',function(event){
    // ngừng việc nổi bọt
    event.stopPropagation()
})
searchHistory.addEventListener('click',function(event){
    // ngừng việc nổi bọt
    event.stopPropagation()
})

//Modal show hide account
const modalAccount = $1('.modal-account')
const iconUsers = $2('.login-link')
const authForms = $2('.auth-form')
const registerForm = $1('.register-form')
const  loginForm = $1('.login-form')
const loginFormBtn = $1('.login-from__btn')
const registerFormBtn = $1('.register-from__btn')
const backBtn= $2('.auth-form__controls-back')
const navMobile = $1('.nav-mobile')
const navBtnMobile = $1('.nav-link')
const forgotBtn = $1('.auth-form__help-link')
const forgotForm = $1('.forgot-pass-form')



function showAccount(){
    modalAccount.classList.add('modal-account__open')
}
function hideAccount(){
    modalAccount.classList.remove('modal-account__open')
}
for(const iconUser of iconUsers) {
    iconUser.addEventListener('click', showAccount)
}
modalAccount.addEventListener('click',hideAccount)

loginFormBtn.addEventListener('click', function (){
    loginForm.style.display="block"
    registerForm.style.display="none"

})
registerFormBtn.addEventListener('click', function (){
    registerForm.style.display="block"
    loginForm.style.display="none"

})
forgotBtn.addEventListener('click', function (){
    loginForm.style.display="none"
    forgotForm.style.display="block"
})

for(const btn of backBtn){
    btn.addEventListener('click', hideAccount)
}

for(const authForm of authForms) {
    authForm.addEventListener('click', function (e) {
        e.stopPropagation()
    })
}


const close = $1('.close');
const overlay = $1('.popup-overlay');
const popup = $1('.popup');

popup.onclick = function (e) {
    e.stopPropagation();
}
close.onclick = function(e) {
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}
overlay.onclick = function (){
    document.querySelector('.stay-in-touch').style.display = 'none';
    overlay.style.display = 'none';
}

const submitLink = $1(".auth-form__controls a");
    submitLink.onclick = function (){
        submitLink.setAttribute("href", "./personalInformation.html");
}

const navMobileOverlay = $1('.nav-mobile__overlay');

navBtnMobile.onclick = function() {
    navMobile.style.display = "block";
    navMobileOverlay.style.display = "block";
}

const iconExit = $1('.icon-exit');

iconExit.onclick = function () {
    navMobile.style.display = "none";
    navMobileOverlay.style.display = "none";
}

navMobileOverlay.onclick = function() {
    navMobile.style.display = "none";
    navMobileOverlay.style.display = "none"; 
}