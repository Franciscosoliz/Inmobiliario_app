from rest_framework import serializers
from mi_app.models import Cita

class CitaSerializer(serializers.ModelSerializer):
    propiedad_titulo = serializers.ReadOnlyField(source='propiedad.titulo')
    cliente_nombre = serializers.ReadOnlyField(source='cliente.nombre_completo')
    agente_nombre = serializers.ReadOnlyField(source='agente.user.get_full_name')

    class Meta:
        model = Cita
        fields = [
            'id', 'propiedad', 'propiedad_titulo', 'cliente', 'cliente_nombre', 
            'agente', 'agente_nombre', 'fecha_hora', 'comentarios', 'estado'
        ]