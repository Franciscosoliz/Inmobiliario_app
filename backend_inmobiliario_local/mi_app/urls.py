from rest_framework import routers
from .views import (
    AgenteViewSet, ZonaViewSet, PropiedadViewSet, 
    ClienteViewSet, CitaViewSet, ContratoViewSet, 
    UserViewSet
)

router = routers.DefaultRouter()

router.register('agentes', AgenteViewSet)
router.register('zonas', ZonaViewSet)
router.register('propiedades', PropiedadViewSet)
router.register('clientes', ClienteViewSet)
router.register('citas', CitaViewSet)
router.register('contratos', ContratoViewSet)
router.register('usuarios', UserViewSet)

urlpatterns = router.urls