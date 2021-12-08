<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$json=array();

if(isset($_GET['aidi'])){
    $id=$_GET['aidi'];
    $conexion=mysqli_connect($hostname,$username,$password,$database);
    $query="SELECT  CONCAT_WS(' ',Usuarios.usuario_nombres,Usuarios.usuario_apellidos) as clase_propietario
                FROM Clases
                JOIN Usuarios
                ON Clases.clase_propietario=Usuarios.usuario_id
                Where Usuarios.usuario_id='{$aidi}'";
    $resul=mysqli_query($conexion,$query);
    if($query){
        if($reg=mysqli_fetch_array($resul)){
            $json['datos'][]=$reg;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
    
    else{
        $results["clase_propietario"]='';
        $json['datos'][]=$results;
        echo json_encode($json);
    }
}
else{
    $results["clase_propietario"]='';
    $json['datos'][]=$results;
    echo json_encode($json);
}
?>