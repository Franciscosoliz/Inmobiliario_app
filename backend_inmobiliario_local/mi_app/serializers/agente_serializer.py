from rest_framework import serializers
from mi_app.models import Agente
from .usuario_serializer import UserSerializer

class AgenteSerializer(serializers.ModelSerializer):
    # Anidamos el UserSerializer para ver los datos del usuario (Nombre, apellido, email)
    user_detail = UserSerializer(source='user', read_only=True)

    class Meta:
        model = Agente
        fields = ['id', 'user', 'user_detail', 'licencia_profesional', 'telefono', 'especialidad', 'activo']
        extra_kwargs = {
            'user_detail': {'read_only': True}
        }