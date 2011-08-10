<?PHP

class MY_Loader extends CI_Loader
{
    function MY_Loader()
    {
        parent::__construct();
    }

    function service($class)
    {
        if ( file_exists(APPPATH.'service/'.$class.EXT) )
        {
            require_once(APPPATH.'service/'.$class.EXT);
	    echo("Loading service: service/".$class.EXT."\n");
        }
        else
        {
            return FALSE;
        }
        // you need to check if the file loaded ready defines the requested class...

	//$class = "service\\" . $class;
        $class = new $class;
        return $class;

    }
}