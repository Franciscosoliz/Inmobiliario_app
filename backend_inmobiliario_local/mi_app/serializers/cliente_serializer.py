from rest_framework import serializers
from mi_app.models import ClienteInmobiliario

class ClienteSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClienteInmobiliario
        fields = ['id', 'nombre_completo', 'identificacion', 'email', 'telefono', 'presupuesto_maximo']