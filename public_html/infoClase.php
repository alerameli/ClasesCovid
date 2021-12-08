<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$json=array();

if(isset($_GET['aidi'])){
    $id=$_GET['aidi'];
    $conexion=mysqli_connect($hostname,$username,$password,$database);
    $query="SELECT 
            Clases.clase_id,
            Clases.clase_nombre, 
            Clases.clase_lugar,
            Clases.clase_fecha,
            Clases.clase_hora,
            CONCAT_WS(' ',Usuarios.usuario_nombres,Usuarios.usuario_apellidos) as clase_propietario,
            Clases.clase_desc,
            Clases.clase_status,
            Clases.clase_contra
            FROM Clases 
            JOIN Usuarios
            ON Usuarios.usuario_id=Clases.clase_propietario
            WHERE Clases.clase_id= '{$id}'";
    $resul=mysqli_query($conexion,$query);
    if($query){
        if($reg=mysqli_fetch_array($resul)){
            $json['datos'][]=$reg;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
    
    else{
        $results["clase_id"]='';
        $results["clase_nombre"]='';
        $results["clase_lugar"]='';
        $results["clase_fecha"]='';
        $results["clase_hora"]='';
        $results["clase_prop"]='';
        $results["clase_desc"]='';
        $results["clase_status"]='';
        $results["clase_contra"]='';
        $json['datos'][]=$results;
        echo json_encode($json);
    }
}
else{
    $results["clase_id"]='';
    $results["clase_nombre"]='';
    $results["clase_lugar"]='';
    $results["clase_fecha"]='';
    $results["clase_hora"]='';
    $results["clase_prop"]='';
    $results["clase_desc"]='';
    $results["clase_status"]='';
    $results["clase_contra"]='';
    $json['datos'][]=$results;
    echo json_encode($json);
}
?>