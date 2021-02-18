package com.davisilvaprojetos.mapas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                mMap.clear();
                /*
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                mMap.clear();
                LatLng localUsuario = new LatLng(latitude, longitude);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(localUsuario)
                                .title("Minha Localização")
                );

                mMap.moveCamera(//2.0 até 21.0
                        CameraUpdateFactory.newLatLngZoom(localUsuario,18));*/


                /*
                    Geocoding -> processo de transformar um endereço
                    ou descrição de um local em latitude/longitude
                    Reverse Geocoding-> processo de transformar latitude/longitude em um endereço
                 */
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    //List<Address> listaEndereco = geocoder.getFromLocation(latitude,longitude,1);
                    String stringEndereco = "Avenida Paulista, 1374 - Bela Vista, São Paulo - SP";
                    List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco,1);
                    if(listaEndereco != null && listaEndereco.size() > 0 ){
                        Address endereco = listaEndereco.get(0);
                        /*
                            onLocationChanged:
                            Address[
                            addressLines=[0:"Av. Pedro Álvares Cabral, portão 3 - Vila Mariana, São Paulo - SP, 04094-050, Brasil"],
                            feature=Vila Mariana,
                            admin=São Paulo,
                            sub-admin=São Paulo,
                            locality=null,
                            thoroughfare=null,
                            postalCode=04094-050,
                            countryCode=BR,
                            countryName=Brasil,
                            hasLatitude=true,
                            latitude=-23.588941700000003,
                            hasLongitude=true,
                            longitude=-46.6561331,
                            phone=null,
                            url=null,
                            extras=null]
                         */
                        Double lat = endereco.getLatitude();
                        Double lon= endereco.getLongitude();
                        LatLng localUsuario = new LatLng(lat, lon);
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(localUsuario)
                                        .title("Minha Localização")
                        );

                        mMap.moveCamera(//2.0 até 21.0
                                CameraUpdateFactory.newLatLngZoom(localUsuario,18));

                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(localUsuario);
                        circleOptions.radius(50);//Medida em metros
                        circleOptions.strokeWidth(1);
                        circleOptions.strokeColor(Color.GRAY);
                        circleOptions.fillColor(Color.argb(128,179, 215, 255)); //0 até 255 para o Alpha
                        mMap.addCircle(circleOptions);

                        //Rota possível destino -23.563661148297033, -46.653814183148924
                        PolygonOptions polygonOptions = new PolygonOptions();
                        polygonOptions.add(new LatLng(lat, lon));
                        polygonOptions.add(new LatLng(-23.5562257335258, -46.66363922692259));
                        polygonOptions.strokeColor(Color.RED);
                        polygonOptions.fillColor(Color.GRAY);
                        mMap.addPolygon(polygonOptions);

                        //System.out.println("Local: "+endereco.getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener

            );
        }

        /*
        //Mudar Exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Add a marker in Sydney and move the camera
        LatLng ibirapuera = new LatLng(-23.587127118985972, -46.657667187845774);
        //(Ibirapuera) Latitude: -23.587127118985972,Longitude: -46.657667187845774

        //Desenhando forma
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(ibirapuera);
        circleOptions.radius(50);//Medida em metros
        circleOptions.strokeWidth(1);
        circleOptions.strokeColor(Color.GRAY);
        circleOptions.fillColor(Color.argb(128,179, 215, 255)); //0 até 255 para o Alpha
        mMap.addCircle(circleOptions);
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(-23.586332, -46.658754));
        polygonOptions.add(new LatLng(-23.585615, -46.656662));
        polygonOptions.add(new LatLng(-23.587158, -46.657037));
        polygonOptions.add(new LatLng(-23.587247, -46.658797));
        polygonOptions.strokeColor(Color.GRAY);
        polygonOptions.fillColor(Color.argb(128,179, 215, 255));
        mMap.addPolygon(polygonOptions);

        //Adicionando evento de clique
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                //Toast.makeText(MapsActivity.this, "OnClick Lat: "+latitude+" Long: "+longitude, Toast.LENGTH_SHORT).show();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(ibirapuera);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(20);
                mMap.addPolyline(polylineOptions);

                mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Local")
                                .snippet("Descrição")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_loja))
                );
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(MapsActivity.this, "OnLong Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_SHORT).show();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Local")
                                .snippet("Descrição")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_carro_roxo_48px)));
            }
        });

        mMap.addMarker(
                new MarkerOptions()
                        .position(ibirapuera)
                        .title("Ibirapuera")
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carro))
        );

        mMap.moveCamera(//2.0 até 21.0
                //CameraUpdateFactory.newLatLng(ibirapuera));
                CameraUpdateFactory.newLatLngZoom(ibirapuera, 18)
        );

         */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar localização do usuário
                /*
                    1- Provedor da Localização
                    2-Tempo mínimo entre atualizações de localização (Em Milesegundos)
                    3- Distância mínima entre atualizações de localização (Metros)
                    4-Location Listener para receber atualizações
                 */

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener

                    );
                }

            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}