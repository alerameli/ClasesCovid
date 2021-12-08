<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$clases=array();
$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['IDUsuario'])){
    $IDUsuario=$_GET['IDUsuario'];
    $query="SELECT DISTINCT Clases.* FROM Asistencias JOIN Clases
            WHERE
                Clases.clase_id = Asistencias.asistencia_clase AND 
                Asistencias.asistencia_usuario = {$IDUsuario};";
    $resul=mysqli_query($conexion,$query);
    $i=0;
    if($query){
        while($reg=mysqli_fetch_array($resul)){
            $clases['datos'][$i]=$reg;
            $i++;
        }
        mysqli_close($conexion);
        echo json_encode($clases);
    }
}else{
    $results["clase_id"]='';
    $results["clase_nombre"]='';
    $results["clase_lugar"]='';
    $results["clase_fecha"]='';
    $results["clase_hora"]='';
    $results["clase_propietario"]='';
    $results["clase_desc"]='';
    $results["clase_status"]='';
    $results["clase_contra"]='';
    $clases['datos'][]=$results;
    echo json_encode($clases);
}
?>