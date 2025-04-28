// Script para mostrar turno y condición asociados al horario seleccionado

document.addEventListener('DOMContentLoaded', function () {
  const horarioSelect = document.getElementById('horario');
  const turnoInput = document.getElementById('turno-horario-info');
  const condicionInput = document.getElementById('condicion-horario-info');

  // Construir un mapa de idHorario -> { turno, condicion }
  let horariosData = {};
  if (window.horariosInfo) {
    horariosData = window.horariosInfo;
  } else if (horarioSelect) {
    // Si no hay variable global, intentar obtener de data-attributes de las opciones
    Array.from(horarioSelect.options).forEach(opt => {
      if (opt.dataset.turno && opt.dataset.condicion) {
        horariosData[opt.value] = {
          turno: opt.dataset.turno,
          condicion: opt.dataset.condicion
        };
      }
    });
  }

  function actualizarCamposInfo() {
    const selectedId = horarioSelect.value;
    if (horariosData[selectedId]) {
      turnoInput.value = horariosData[selectedId].turno;
      condicionInput.value = horariosData[selectedId].condicion;
    } else {
      turnoInput.value = '';
      condicionInput.value = '';
    }
  }

  if (horarioSelect) {
    horarioSelect.addEventListener('change', actualizarCamposInfo);
    // Inicializar al cargar
    actualizarCamposInfo();
  }
});
