from rest_framework import serializers
from mi_app.models import Contrato

class ContratoSerializer(serializers.ModelSerializer):
    propiedad_titulo = serializers.ReadOnlyField(source='propiedad.titulo')
    cliente_nombre = serializers.ReadOnlyField(source='cliente.nombre_completo')

    class Meta:
        model = Contrato
        fields = ['id', 'propiedad', 'propiedad_titulo', 'cliente', 'cliente_nombre', 'tipo', 'monto_total_final', 'fecha_firma', 'vigente']