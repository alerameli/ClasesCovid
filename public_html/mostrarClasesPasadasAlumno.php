<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$clases=array();
$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['AIDI'])){
    $AIDI=$_GET['AIDI'];
    $query="Select 
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
            JOIN Asistencias
            JOIN Usuarios
            ON Asistencias.asistencia_clase=Clases.clase_id
            AND Clases.clase_status!='proxima'
            AND Asistencias.asistencia_usuario='{$AIDI}'
            AND Usuarios.usuario_id=Clases.clase_propietario";
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
    $results["clase_prop"]='';
    $results["clase_desc"]='';
    $results["clase_status"]='';
    $results["clase_contra"]='';
    $clases['datos'][]=$results;
    echo json_encode($clases);
}
?>