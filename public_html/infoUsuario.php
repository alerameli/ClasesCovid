<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$json=array();

if(isset($_GET['AIDI'])){
    $id=$_GET['AIDI'];
    $conexion=mysqli_connect($hostname,$username,$password,$database);
    $query="SELECT *
            FROM Usuarios 
            WHERE usuario_id= '{$id}'";
    $resul=mysqli_query($conexion,$query);
    if($query){
        if($reg=mysqli_fetch_array($resul)){
            $json['datos'][]=$reg;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
    
    else{
        $results["usuario_id"]='';
        $results["usuario_nombres"]='';
        $results["usuario_apellidos"]='';
        $results["usuario_correo"]='';
        $results["usuario_celular"]='';
        $results["usuario_tipo"]='';
        $results["usuario_usuario"]='';
        $results["usuario_contraseña"]='';
        $json['datos'][]=$results;
        echo json_encode($json);
    }
}
else{
    $results["usuario_id"]='';
        $results["usuario_nombres"]='';
        $results["usuario_apellidos"]='';
        $results["usuario_correo"]='';
        $results["usuario_celular"]='';
        $results["usuario_tipo"]='';
        $results["usuario_usuario"]='';
        $results["usuario_contraseña"]='';
        $json['datos'][]=$results;
        echo json_encode($json);
}
?>