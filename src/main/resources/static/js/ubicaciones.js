$(document).ready(function() {
    $('#departamento').change(function() {
        var departamentoId = $(this).val();
        if (departamentoId) {
            $.ajax({
                url: '/ubicaciones/provincias/' + departamentoId,
                type: 'GET',
                success: function(data) {
                    $('#provincia').empty();
                    $('#provincia').append('<option value="">Selecciona una provincia</option>');
                    $.each(data, function(index, provincia) {
                        $('#provincia').append('<option value="' + provincia.id + '">' + provincia.name + '</option>');
                    });
                    $('#distrito').empty();
                    $('#distrito').append('<option value="">Selecciona un distrito</option>');
                }
            });
        } else {
            $('#provincia').empty();
            $('#distrito').empty();
        }
    });

    $('#provincia').change(function() {
        var provinciaId = $(this).val();
        if (provinciaId) {
            $.ajax({
                url: '/ubicaciones/distritos/' + provinciaId,
                type: 'GET',
                success: function(data) {
                    $('#distrito').empty();
                    $('#distrito').append('<option value="">Selecciona un distrito</option>');
                    $.each(data, function(index, distrito) {
                        $('#distrito').append('<option value="' + distrito.id + '">' + distrito.name + '</option>');
                    });
                }
            });
        } else {
            $('#distrito').empty();
        }
    });
});