$(document).ready(function () {
  $("#departamento").change(function () {
    var departamentoId = $(this).val();
    if (departamentoId) {
      $.ajax({
        url: "/ubicaciones/provincias/" + departamentoId,
        type: "GET",
        success: function (data) {
          $("#provincia").empty();
          $("#provincia").append(
            '<option value="">Selecciona una provincia</option>'
          );
          $.each(data, function (index, provincia) {
            $("#provincia").append(
              '<option value="' +
                provincia.id +
                '">' +
                provincia.name +
                "</option>"
            );
          });
          $("#distrito").empty();
          $("#distrito").append(
            '<option value="">Selecciona un distrito</option>'
          );
        },
      });
    } else {
      $("#provincia").empty();
      $("#distrito").empty();
    }
  });

  $("#provincia").change(function () {
    var provinciaId = $(this).val();
    if (provinciaId) {
      $.ajax({
        url: "/ubicaciones/distritos/" + provinciaId,
        type: "GET",
        success: function (data) {
          $("#distrito").empty();
          $("#distrito").append(
            '<option value="">Selecciona un distrito</option>'
          );
          $.each(data, function (index, distrito) {
            $("#distrito").append(
              '<option value="' +
                distrito.id +
                '">' +
                distrito.name +
                "</option>"
            );
          });
        },
      });
    } else {
      $("#distrito").empty();
    }
  });

  $("#campana").change(function () {
    var campanaId = $(this).val();
    if (campanaId) {
      $.ajax({
        url: "/ubicaciones/segmentos/" + campanaId,
        type: "GET",
        success: function (data) {
          $("#segmento").empty();
          $("#segmento").append(
            '<option value="">Selecciona un segmento</option>'
          );
          $.each(data, function (index, segmento) {
            $("#segmento").append(
              '<option value="' +
                segmento.id +
                '">' +
                segmento.name +
                "</option>"
            );
          });
        },
      });
    } else {
      $("#segmento").empty();
    }
  });
  $("#segmento").change(function () {
    var segmentoId = $(this).val();
    if (segmentoId) {
      $.ajax({
        url: "/ubicaciones/gestiones/" + segmentoId,
        type: "GET",
        success: function (data) {
          $("#gestion").empty();
          $("#gestion").append(
            '<option value="">Selecciona un tipo de gestión</option>'
          );
          $.each(data, function (index, gestion) {
            $("#gestion").append(
              '<option value="' + gestion.id + '">' + gestion.name + "</option>"
            );
          });
        },
      });
    } else {
      $("#gestion").empty();
    }
  });
});
