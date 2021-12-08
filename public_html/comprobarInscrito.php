<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$conexion=mysqli_connect($hostname,$username,$password,$database);

$usuario=$_GET["usuario"];
$clase=$_GET["clase"];
$json=array();


$sql="SELECT IF (EXISTS (SELECT Clases.clase_nombre, Usuarios.usuario_nombres 
        from Usuarios
        JOIN Asistencias
        JOIN Clases
        ON Asistencias.asistencia_clase=Clases.clase_id
        AND Usuarios.usuario_id=Asistencias.asistencia_usuario
        WHERE Asistencias.asistencia_clase='{$clase}'
        AND Usuarios.usuario_id='{$usuario}'),1,0) as existe";
$resul=mysqli_query($conexion,$sql);

if($sql){
        if($reg=mysqli_fetch_array($resul)){
            $json['datos'][]=$reg;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
    else{
        $results["existe"]='';
        $json['datos'][]=$results;
        mysqli_close($conexion);
        echo json_encode($json);
    }
?>

