using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace demoflavius_WP8.Helpers
{
    public class WP8ImageConverter : IValueConverter
    {

        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            try
            {
                string path = (String)value;
                Uri uri = new Uri(path, UriKind.Absolute);
                ImageSource imgSource = new BitmapImage(uri);
                return imgSource;
            }
            catch(Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return null;
        }
    }
}
