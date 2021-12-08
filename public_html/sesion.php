<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$json=array();
	if(isset($_GET["usuarioUser"]) && isset($_GET["usuarioPass"])){
		$user=$_GET['usuarioUser'];
		$pwd=$_GET['usuarioPass'];
		
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="SELECT usuarioUser, usuarioPass, UsuarioName FROM usuarios WHERE usuarioUser= '{$user}' AND usuarioPass = '{$pwd}'";
		$resultado=mysqli_query($conexion,$consulta);

		if($consulta){
		
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}

		else{
			$results["usuarioUser"]='';
			$results["usuarioPass"]='';
			$results["UsuarioName"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
	}
	else{
		   	$results["usuarioUser"]='';
			$results["usuarioPass"]='';
			$results["UsuarioName"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>