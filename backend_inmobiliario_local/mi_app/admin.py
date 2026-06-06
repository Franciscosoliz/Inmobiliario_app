from django.contrib import admin
from .models import Agente, Zona, Propiedad, ClienteInmobiliario, Cita, Contrato

admin.site.register(Zona)
admin.site.register(ClienteInmobiliario)



@admin.register(Agente)
class AgenteAdmin(admin.ModelAdmin):
    # Mostramos los datos clave del agente y si está activo
    list_display = ('id', 'get_nombre_completo', 'licencia_profesional', 'telefono', 'especialidad', 'activo')
    list_filter = ('activo', 'especialidad')
    search_fields = ('user__first_name', 'user__last_name', 'licencia_profesional')
    list_editable = ('activo', 'especialidad') # Permite activar/desactivar agentes rápido

    # Método de apoyo para jalar el nombre desde la tabla User de Django
    def get_nombre_completo(self, obj):
        return f"{obj.user.first_name} {obj.user.last_name}"
    get_nombre_completo.short_description = 'Nombre del Agente'


@admin.register(Propiedad)
class PropiedadAdmin(admin.ModelAdmin):
    # Ideal para ver el catálogo rápido, filtrar por precios y tipos
    list_display = ('titulo', 'tipo_inmueble', 'estado_negocio', 'precio', 'zona', 'agente')
    list_filter = ('tipo_inmueble', 'estado_negocio', 'zona')
    search_fields = ('titulo', 'direccion', 'descripcion')
    list_editable = ('estado_negocio', 'precio') # Facilita cambiar de "Disponible" a "Vendido"


@admin.register(Cita)
class CitaAdmin(admin.ModelAdmin):
    # Control de la agenda de visitas
    list_display = ('fecha_hora', 'propiedad', 'cliente', 'agente', 'estado')
    list_filter = ('estado', 'fecha_hora')
    search_fields = ('propiedad__titulo', 'cliente__nombre_completo', 'agente__user__first_name')
    list_editable = ('estado',)


@admin.register(Contrato)
class ContratoAdmin(admin.ModelAdmin):
    # Cierre de negocios financieros
    list_display = ('id', 'propiedad', 'cliente', 'tipo', 'monto_total_final', 'fecha_firma', 'vigente')
    list_filter = ('tipo', 'vigente', 'fecha_firma')
    search_fields = ('propiedad__titulo', 'cliente__nombre_completo')
    list_editable = ('vigente',)
    readonly_fields = ('fecha_firma',) # La fecha se pone sola al firmar