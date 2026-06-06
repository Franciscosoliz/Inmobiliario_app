from rest_framework import serializers
from mi_app.models import Propiedad

class PropiedadSerializer(serializers.ModelSerializer):
    # Campos informativos de apoyo para los listados en Kotlin
    zona_nombre = serializers.ReadOnlyField(source='zona.nombre')
    agente_nombre = serializers.ReadOnlyField(source='agente.user.get_full_name')

    class Meta:
        model = Propiedad
        fields = [
            'id', 'titulo', 'descripcion', 'tipo_inmueble', 'estado_negocio', 
            'precio', 'direccion', 'habitaciones', 'banos', 'area_metros', 
            'agente', 'agente_nombre', 'zona', 'zona_nombre', 'imagen'
        ]