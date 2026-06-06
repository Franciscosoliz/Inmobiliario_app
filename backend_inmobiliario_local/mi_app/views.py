from mi_app.serializers.usuario_serializer import UserSerializer
from rest_framework import viewsets, filters, status
from rest_framework.decorators import action, api_view, permission_classes
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated, AllowAny, BasePermission
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from django.contrib.auth.models import User

# Importación de nuestros nuevos serializadores inmobiliarios
from .serializers.agente_serializer import AgenteSerializer
from .serializers.zona_serializer import ZonaSerializer
from .serializers.propiedad_serializer import PropiedadSerializer
from .serializers.cliente_serializer import ClienteSerializer
from .serializers.cita_serializer import CitaSerializer
from .serializers.contrato_serializer import ContratoSerializer

from .models import Agente, Zona, Propiedad, ClienteInmobiliario, Cita, Contrato

class IsAdminOrReadOnly(BasePermission):
    def has_permission(self, request, view):
        if request.method in ['GET', 'HEAD', 'OPTIONS']:
            return request.user and request.user.is_authenticated
        return request.user and request.user.is_authenticated and request.user.is_staff


class BaseInmobiliariaViewSet(viewsets.ModelViewSet):
    filter_backends = [filters.SearchFilter]
    # permission_classes = [IsAdminOrReadOnly]
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción


class AgenteViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = Agente.objects.all()
    serializer_class = AgenteSerializer
    search_fields = ['user__first_name', 'user__last_name', 'licencia_profesional']


class ZonaViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = Zona.objects.all()
    serializer_class = ZonaSerializer
    search_fields = ['nombre', 'ciudad']


class PropiedadViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = Propiedad.objects.all()
    serializer_class = PropiedadSerializer
    search_fields = ['titulo', 'direccion', 'tipo_inmueble']

    @action(detail=False, methods=['get'])
    def disponibles(self, request):
        disponibles = Propiedad.objects.filter(estado_negocio='Disponible')
        page = self.paginate_queryset(disponibles)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)
        serializer = self.get_serializer(disponibles, many=True)
        return Response(serializer.data)


class ClienteViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = ClienteInmobiliario.objects.all()
    serializer_class = ClienteSerializer
    search_fields = ['nombre_completo', 'identificacion', 'email']


class CitaViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = Cita.objects.all()
    serializer_class = CitaSerializer
    search_fields = ['propiedad__titulo', 'cliente__nombre_completo', 'estado']


class ContratoViewSet(BaseInmobiliariaViewSet):
    permission_classes = [AllowAny]  # Temporalmente abierto para desarrollo, ajustar en producción
    queryset = Contrato.objects.all()
    serializer_class = ContratoSerializer
    search_fields = ['propiedad__titulo', 'cliente__nombre_completo', 'tipo']


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated]


@api_view(['POST'])
@permission_classes([AllowAny]) 
def registro_agente(request):
    username = request.data.get('username')
    email = request.data.get('email')
    password = request.data.get('password')
    licencia = request.data.get('licencia_profesional')
    telefono = request.data.get('telefono')

    if not all([username, email, password, licencia, telefono]):
        return Response({"error": "Faltan campos obligatorios para registrar al Agente"}, status=status.HTTP_400_BAD_REQUEST)

    if User.objects.filter(username=username).exists():
        return Response({"error": "El nombre de usuario ya está registrado"}, status=status.HTTP_400_BAD_REQUEST)

    # Creamos el usuario base marcándolo como staff (Admin en la App Móvil)
    user = User.objects.create_user(username=username, email=email, password=password, is_staff=True)
    
    # Creamos automáticamente su perfil inmobiliario de Agente
    agente = Agente.objects.create(user=user, licencia_profesional=licencia, telefono=telefono)

    return Response({
        "message": "Agente y Usuario Administrador creados con éxito",
        "user_id": user.id,
        "agente_id": agente.id
    }, status=status.HTTP_201_CREATED)


class CustomAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data, context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token, created = Token.objects.get_or_create(user=user)
        
        # Esta respuesta calza exactamente con lo que espera tu app Android en Kotlin
        return Response({
            'access': token.key,       # Mapeado como 'access' para simular comportamiento JWT estándar
            'user_id': user.pk,
            'is_staff': user.is_staff, # True si es Admin, False si es Consultor básico
            'email': user.email,
            'username': user.username
        })