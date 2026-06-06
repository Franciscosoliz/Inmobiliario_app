from django.contrib import admin
from django.urls import path, include
from mi_app import views
from django.views.generic.base import RedirectView
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    # Al entrar a la raíz, te redirige automáticamente al panel de administración
    path('', RedirectView.as_view(url='admin/', permanent=True)),

    # Panel de administración de Django (donde verás las 7 tablas inmobiliarias)
    path('admin/', admin.site.urls),

    # Incluye de golpe todas las rutas inmobiliarias (agentes, propiedades, zonas, etc.)
    path('api/', include('mi_app.urls')),

    # Endpoints de autenticación y seguridad acoplados con tu App en Kotlin
    path('api/token/', views.CustomAuthToken.as_view(), name='api_token'),
    path('api/registro/', views.registro_agente, name='api_registro'),

    # Login opcional para la interfaz web navegable de DRF
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]

# Esto permite que Django sirva las fotos de las propiedades de forma local
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)