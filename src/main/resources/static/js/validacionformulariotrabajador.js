document.addEventListener("DOMContentLoaded", function () {
  const tipoDocumento = document.getElementById("tipoDocumento");
  const documento = document.getElementById("documento");
  const correo = document.getElementById("correo");
  const telefono = document.getElementById("telefono");

  if (tipoDocumento && documento && correo && telefono) {
    tipoDocumento.addEventListener("change", function () {
      const selectedTipo = tipoDocumento.options[tipoDocumento.selectedIndex].text;
      if (selectedTipo === "DNI") {
        documento.setAttribute("maxlength", "8");
        documento.setAttribute("pattern", "\\d{8}");
        documento.setAttribute("title", "Debe contener 8 dígitos numéricos");
      } else {
        documento.setAttribute("maxlength", "9");
        documento.setAttribute("pattern", "\\d{9}");
        documento.setAttribute("title", "Debe contener 9 dígitos numéricos");
      }
    });

    // Restringir la entrada a solo números para el documento
    documento.addEventListener("input", function (e) {
      documento.value = documento.value.replace(/\D/g, "");
    });

    // Restringir la entrada a solo números para el telefono
    telefono.addEventListener("input", function (e) {
      telefono.value = telefono.value.replace(/\D/g, "");
      telefono.setAttribute("maxlength", "9");
    });

    // Restringir la entrada a caracteres válidos para el correo electrónico excluyendo % y +
    correo.addEventListener("input", function (e) {
      correo.value = correo.value.replace(/[^a-zA-Z0-9._@]/g, "");
    });

    // Validar al enviar el formulario
    document.querySelector("form").addEventListener("submit", function (event) {
      const emailPattern = /^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!documento.checkValidity()) {
        event.preventDefault();
        alert(documento.validationMessage);
      }
      if (!correo.checkValidity() || !emailPattern.test(correo.value)) {
        event.preventDefault();
        alert("Debe ser un correo electrónico válido (por ejemplo, usuario@dominio.com)");
      }
    });
  } else {
    console.error("Alguno de los elementos no se encontró en el DOM.");
  }
});

