const hamBurger = document.querySelector("#toggle-btn");

hamBurger.addEventListener("click", function () {
  document.querySelector("#sidebar").classList.toggle("expand");
  document.querySelector(".main").classList.toggle("expanded");
  document.querySelector(".navbar").classList.toggle("expanded");
});
