using demo_flavius_Core.Interfaces;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;

namespace demoflavius.Helpers
{
    public class ApplicationDataProvider : IApplicationDataProvider
    {
        private static ApplicationDataContainer _localSettings = Windows.Storage.ApplicationData.Current.LocalSettings;

        public void StoreValue(string key, object value)
        {
            try
            {
                _localSettings.Values[key] = JsonConvert.SerializeObject(value);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public object GetValue(string key)
        {
            try
            {
                return _localSettings.Values[key];
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        public void RemoveEntry(string key)
        {
            try
            {
                _localSettings.Values.Remove(key);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}
