from rest_framework import serializers
from mi_app.models import Pedido
from mi_app.serializers.propiedad_serializer import DetallePedidoSerializer


class PedidoSerializer(serializers.ModelSerializer):
    detalles = DetallePedidoSerializer(many=True, read_only=True)
    cliente_nombre = serializers.ReadOnlyField(source='cliente.nombre')

    class Meta:
        model = Pedido
        fields = '__all__'