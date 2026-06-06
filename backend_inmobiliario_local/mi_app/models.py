from django.db import models
from django.contrib.auth.models import User

class Agente(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='agente_perfil')
    licencia_profesional = models.CharField(max_length=50, unique=True)
    telefono = models.CharField(max_length=20)
    especialidad = models.CharField(max_length=50, default='General')  # Ventas, Arriendos, Residencial
    activo = models.BooleanField(default=True)

    def __str__(self):
        return f"{self.user.first_name} {self.user.last_name} - {self.licencia_profesional}"


class Zona(models.Model):
    nombre = models.CharField(max_length=100, unique=True)
    ciudad = models.CharField(max_length=100, default='Quito')
    codigo_postal = models.CharField(max_length=15, blank=True, null=True)

    def __str__(self):
        return f"{self.nombre} ({self.ciudad})"
    


class Propiedad(models.Model):
    TIPO_CHOICES = (
        ('Casa', 'Casa'),
        ('Departamento', 'Departamento'),
        ('Terreno', 'Terreno'),
        ('Oficina', 'Oficina'),
    )
    ESTADO_CHOICES = (
        ('Disponible', 'Disponible'),
        ('Arrendado', 'Arrendado'),
        ('Vendido', 'Vendido'),
    )
    titulo = models.CharField(max_length=200)
    descripcion = models.TextField(blank=True, null=True)
    tipo_inmueble = models.CharField(max_length=20, choices=TIPO_CHOICES, default='Departamento')
    estado_negocio = models.CharField(max_length=20, choices=ESTADO_CHOICES, default='Disponible')
    precio = models.DecimalField(max_digits=12, decimal_places=2)
    direccion = models.CharField(max_length=250)
    habitaciones = models.IntegerField(default=0)
    banos = models.IntegerField(default=0)
    area_metros = models.DecimalField(max_digits=8, decimal_places=2)
    
    agente = models.ForeignKey(Agente, on_delete=models.PROTECT, related_name='propiedades')
    zona = models.ForeignKey(Zona, on_delete=models.PROTECT, related_name='propiedades')
    imagen = models.ImageField(upload_to="propiedades/", null=True, blank=True)

    def __str__(self):
        return f"{self.titulo} - ${self.precio}"


class ClienteInmobiliario(models.Model):
    nombre_completo = models.CharField(max_length=200)
    identificacion = models.CharField(max_length=20, unique=True) # Cédula o RUC
    email = models.EmailField(unique=True)
    telefono = models.CharField(max_length=20)
    presupuesto_maximo = models.DecimalField(max_digits=12, decimal_places=2, blank=True, null=True)

    def __str__(self):
        return self.nombre_completo


class Cita(models.Model):
    ESTADO_CITA = (
        ('Programada', 'Programada'),
        ('Realizada', 'Realizada'),
        ('Cancelada', 'Cancelada'),
    )
    propiedad = models.ForeignKey(Propiedad, on_delete=models.CASCADE, related_name='citas')
    cliente = models.ForeignKey(ClienteInmobiliario, on_delete=models.CASCADE, related_name='citas')
    agente = models.ForeignKey(Agente, on_delete=models.CASCADE, related_name='citas')
    fecha_hora = models.DateTimeField()
    comentarios = models.TextField(blank=True, null=True)
    estado = models.CharField(max_length=15, choices=ESTADO_CITA, default='Programada')

    def __str__(self):
        return f"Cita: {self.cliente.nombre_completo} -> {self.propiedad.titulo}"


class Contrato(models.Model):
    TIPO_CONTRATO = (
        ('Compraventa', 'Compraventa'),
        ('Arrendamiento', 'Arrendamiento'),
    )
    propiedad = models.OneToOneField(Propiedad, on_delete=models.PROTECT, related_name='contrato')
    cliente = models.ForeignKey(ClienteInmobiliario, on_delete=models.PROTECT, related_name='contratos')
    tipo = models.CharField(max_length=20, choices=TIPO_CONTRATO)
    monto_total_final = models.DecimalField(max_digits=12, decimal_places=2)
    fecha_firma = models.DateField(auto_now_add=True)
    vigente = models.BooleanField(default=True)

    def __str__(self):
        return f"Contrato {self.tipo} - Propiedad ID: {self.propiedad.id}"