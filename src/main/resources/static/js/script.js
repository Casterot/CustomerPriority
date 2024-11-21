const hamBurger = document.querySelector("#toggle-btn");

hamBurger.addEventListener("click", function () {
  document.querySelector("#sidebar").classList.toggle("expand");
});

document.addEventListener("DOMContentLoaded", function () {
    const departamentoSelect = document.getElementById('departamento');
    const provinciaSelect = document.getElementById('provincia');
    const distritoSelect = document.getElementById('distrito');

    function resetSelect(selectElement, placeholder) {
        selectElement.innerHTML = `<option value="">${placeholder}</option>`;
        selectElement.disabled = true;
    }

    // Cargar provincias cuando se selecciona un departamento
    departamentoSelect.addEventListener('change', function () {
        const departamentoId = departamentoSelect.value;
        if (departamentoId) {
            fetch(`/ubicaciones/provincias/${departamentoId}`)
                .then(response => response.json())
                .then(data => {
                    resetSelect(provinciaSelect, 'Seleccione una provincia');
                    data.forEach(provincia => {
                        const option = document.createElement('option');
                        option.value = provincia.idProvincia;
                        option.textContent = provincia.provincia;
                        provinciaSelect.appendChild(option);
                    });
                    provinciaSelect.disabled = false;
                    resetSelect(distritoSelect, 'Seleccione un distrito');
                })
                .catch(error => {
                    console.error('Error al cargar provincias:', error);
                    alert('Hubo un problema al cargar las provincias. Intente nuevamente.');
                });
        } else {
            resetSelect(provinciaSelect, 'Seleccione una provincia');
            resetSelect(distritoSelect, 'Seleccione un distrito');
        }
    });

    // Cargar distritos cuando se selecciona una provincia
    provinciaSelect.addEventListener('change', function () {
        const provinciaId = provinciaSelect.value;
        if (provinciaId) {
            fetch(`/ubicaciones/distritos/${provinciaId}`)
                .then(response => response.json())
                .then(data => {
                    resetSelect(distritoSelect, 'Seleccione un distrito');
                    data.forEach(distrito => {
                        const option = document.createElement('option');
                        option.value = distrito.idDistrito;
                        option.textContent = distrito.distrito;
                        distritoSelect.appendChild(option);
                    });
                    distritoSelect.disabled = false;
                })
                .catch(error => {
                    console.error('Error al cargar distritos:', error);
                    alert('Hubo un problema al cargar los distritos. Intente nuevamente.');
                });
        } else {
            resetSelect(distritoSelect, 'Seleccione un distrito');
        }
    });
});
