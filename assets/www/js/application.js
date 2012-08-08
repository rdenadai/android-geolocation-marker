var coords = "", map = null, myLatlng = null, markers = [];

var clearMarkers = function() {
	for( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap( null );
    }
};

function clearAll() {
	clearMarkers();
	clearCoords();
	$( ".alert" ).hide();
};

function createMap() {
	myLatlng = new google.maps.LatLng( -22.752280, -47.352802 );
	var myOptions = {
			zoom: 18,
			center:myLatlng,
			zoomControl: false,
			panControl: false,
			mapTypeControl:true,
			mapTypeId: google.maps.MapTypeId.HYBRID
		};
		// Cria o mapa e centraliza em um ponto
		map = new google.maps.Map( document.getElementById( "map_canvas" ), myOptions );
}

function clearCoords() {
	$( "#latitude" ).html( "" );
	$( "#longitude" ).html( "" );
};

function success() {
	$( ".alert-success" ).show();
};

function error(error) {
	$( ".alert-error" ).show();
};

function fail(error) {
	$( ".alert-info" ).show();
};

function appendFile( f ) {
	f.createWriter( function( writerOb ) {
		writerOb.onwrite = function() {
			success();
		}
		//go to the end of the file...
		writerOb.seek( writerOb.length );
		writerOb.write( $( "#texto" ).val() + ';' + coords + "\n" );
	});
};
 
function doAppendFile( e ) {
	window.requestFileSystem(
		LocalFileSystem.PERSISTENT, 0, 
		function( fileSystem ) {
			fileSystem.root.getFile( "AndroidGeolocationMarker.csv", { create: true, exclusive: false }, appendFile, fail );
		}
		,fail
	);
};

function readFile( f ) {
	reader = new FileReader();
	reader.onloadend = function(e) {
		success();
	}
	reader.readAsText( f );
};

function doReadFile( e ) {
	window.requestFileSystem( LocalFileSystem.PERSISTENT, 0, 
		function( fileSystem ) {
			fileSystem.root.getFile( "AndroidGeolocationMarker.csv", { create:true }, readFile, onError );
		}
		,fail
	);
};

function doDeleteFile( e ) {
	window.requestFileSystem( LocalFileSystem.PERSISTENT, 0, 
		function( fileSystem ) {
			fileSystem.root.getFile( "AndroidGeolocationMarker.csv", { create:true }, function( f ) {
				f.remove( function() {
					success();
				});
			}
			,fail);
		}
		,fail
	);
};

function getNativeGPS() {
	var lat = NativeGPS.getLat(), lng = NativeGPS.getLong();
	coords = lat + ';' + lng;
	$( "#latitude" ).html( lat );
	$( "#longitude" ).html( lng );
	myLatlng = new google.maps.LatLng( lat, lng );
	var marker = new google.maps.Marker( { position: myLatlng, map: map } );
	markers.push( marker );
	map.setCenter( marker.getPosition() );
};