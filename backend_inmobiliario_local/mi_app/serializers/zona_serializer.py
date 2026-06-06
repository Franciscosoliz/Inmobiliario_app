from rest_framework import serializers
from mi_app.models import Zona

class ZonaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Zona
        fields = ['id', 'nombre', 'ciudad', 'codigo_postal']