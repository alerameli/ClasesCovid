<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$clases=array();

$Abort = false;

$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['TODAY']) && isset($_GET['BEFORE5DAYS']) && isset($_GET['AIDI'])){
    $IDUsuario = $_GET['AIDI'];
    $BEFORE5DAYS = $_GET['BEFORE5DAYS'];
    $TODAY = $_GET['TODAY'];
    
    $query="SELECT DISTINCT
                	Usuarios.usuario_id,
                    Usuarios.usuario_nombres,
                    Usuarios.usuario_apellidos,
                    Usuarios.usuario_correo,
                    Usuarios.usuario_celular,
                    Usuarios.usuario_tipo,
                    Usuarios.usuario_usuario,
                    Usuarios.usuario_contraseña
                FROM Usuarios
                JOIN Alertas
                ON Alertas.alerta_usuario=Usuarios.usuario_id
                WHERE EXISTS(SELECT * 
                                FROM Asistencias
                                Where Asistencias.asistencia_usuario = Usuarios.usuario_id
                                AND asistencia_clase IN(SELECT asistencia_clase
                                                        FROM Asistencias
                                                        WHERE asistencia_usuario = '{$IDUsuario}'))
                AND Usuarios.usuario_id<>'{$IDUsuario}'
                AND Alertas.alerta_positivo='positivo'
                AND Alertas.alerta_fecha BETWEEN '{$BEFORE5DAYS}' AND '{$TODAY}'";
    $result=mysqli_query($conexion,$query);
    $i=0;
    if($result){
        while($reg=mysqli_fetch_array($result)){
            $clases['datos'][$i] = $reg;
            $i++;
        }
    }else
        $Abort = true;
    
}else
    $Abort = true;
    
if($Abort){
    $result = array();
    $result['usuario_id'] = '';
    $result['usuario_nombres'] = '';
    $result['usuario_apellidos'] = '';
    $result['usuario_correo'] = '';
    $result['usuario_celular'] = '';
    $result['usuario_tipo'] = '';
    $result['usuario_usuario'] = '';
    $result['usuario_contraseña'] = '';
    $clases['datos'][] = $result;
}

mysqli_close($conexion);
echo json_encode($clases);
?>