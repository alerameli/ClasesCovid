<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$conexion=mysqli_connect($hostname,$username,$password,$database);
$nombres=$_POST["MandaNombres"];
$apellidos=$_POST["MandaApellidos"];
$correo=$_POST["MandaCorreo"];
$celular=$_POST["MandaCelular"];
$tipo=$_POST["MandaTipo"];
$usuario=$_POST["MandaUsuario"];
$contrasena=$_POST["MandaContraseña"];

$sql="insert into Usuarios(usuario_nombres,usuario_apellidos,usuario_correo,usuario_celular,
                            usuario_tipo,usuario_usuario,usuario_contraseña) 
                            values('$nombres','$apellidos','$correo','$celular','$tipo','$usuario','$contrasena')";
$resul=mysqli_query($conexion,$sql);
echo $resul;
mysqli_close($conexion);
?>



