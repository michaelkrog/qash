<?php

use Doctrine\Common\ClassLoader,
    Doctrine\ORM\Configuration,
    Doctrine\ORM\EntityManager,
    Doctrine\Common\Cache\ArrayCache,
    Doctrine\DBAL\Logging\EchoSqlLogger;

class Doctrine {

  public $em = null;

  public function __construct()
  {

    //global $db;

    // load database configuration from CodeIgniter
    require_once(APPPATH.'config/database.php');
    //die("test:".$db['default']['username'] );
    // Set up class loading. You could use different autoloaders, provided by your favorite framework,
    // if you want to.
    require_once(APPPATH.'libraries/Doctrine/Common/ClassLoader.php');

    $doctrineClassLoader = new ClassLoader('Doctrine',  APPPATH.'libraries');
    $doctrineClassLoader->register();
    $entitiesClassLoader = new ClassLoader('entities', rtrim(APPPATH, '/'));
    $entitiesClassLoader->register();
    $proxiesClassLoader = new ClassLoader('Proxies', APPPATH.'/');
    $proxiesClassLoader->register();

    // Set up caches
    $config = new Configuration;
    $cache = new ArrayCache;
    $config->setMetadataCacheImpl($cache);
    $config->setQueryCacheImpl($cache);

    // Set up driver
    $Doctrine_AnnotationReader = new \Doctrine\Common\Annotations\AnnotationReader($cache);
    $Doctrine_AnnotationReader->setDefaultAnnotationNamespace('Doctrine\ORM\Mapping\\');
    $driver = new \Doctrine\ORM\Mapping\Driver\AnnotationDriver($Doctrine_AnnotationReader, APPPATH.'entities');
    $config->setMetadataDriverImpl($driver);

    // Proxy configuration
    $config->setProxyDir(APPPATH.'/proxies');
    $config->setProxyNamespace('Proxies');

    // Set up logger
    //$logger = new EchoSqlLogger;
    //$config->setSqlLogger($logger);

    $config->setAutoGenerateProxyClasses( TRUE );

    // Database connection information
    $connectionOptions = array(
        'driver' => 'pdo_mysql',
        'user' =>     'root',//$db['default']['username'],
        'password' => 'vanessa73', //$db['default']['password'],
        'host' =>     '127.0.0.1',//$db['default']['hostname'],
        'dbname' =>   'store_v2' //$db['default']['database']
    );

    // Create EntityManager
    $this->em = EntityManager::create($connectionOptions, $config);
  }
}