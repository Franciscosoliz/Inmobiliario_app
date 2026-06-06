from django.contrib.auth.models import User
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        # 1. Agregamos 'password' a los campos
        fields = ['id', 'username', 'email', 'is_staff', 'password']
        extra_kwargs = {
            # 2. 'write_only' asegura que la contraseña se pueda enviar 
            # pero NUNCA se devuelva en los JSON de respuesta.
            'password': {'write_only': True, 'required': False}
        }

    def create(self, validated_data):
        # 3. Usamos create_user para que Django encripte la clave automáticamente
        return User.objects.create_user(**validated_data)

    def update(self, instance, validated_data):
        # 4. Lógica especial para actualizar contraseña si viene en el request
        password = validated_data.pop('password', None)
        user = super().update(instance, validated_data)
        if password:
            user.set_password(password)
            user.save()
        return user