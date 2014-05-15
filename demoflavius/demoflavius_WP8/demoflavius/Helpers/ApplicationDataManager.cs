using demo_flavius_Core.Interfaces;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;

namespace demoflavius_WP8.Helpers
{
    class ApplicationDataManager
    {
        private static IApplicationDataProvider applicationDataProvider = new ApplicationDataProvider();

        public static void StoreValue(string key, object value)
        {
            try
            {
                applicationDataProvider.StoreValue(key, JsonConvert.SerializeObject(value));
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public static object GetValue(string key)
        {
            try
            {
                return applicationDataProvider.GetValue(key);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        public static void RemoveEntry(string key)
        {
            try
            {
                applicationDataProvider.RemoveEntry(key);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

    }
}
