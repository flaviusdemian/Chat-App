using demo_flavius_Core.Interfaces;
using System;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;

namespace demoflavius_WP8.Helpers
{
    public class ApplicationDataProvider : IApplicationDataProvider
    {
        private static IsolatedStorageSettings _localSettings = IsolatedStorageSettings.ApplicationSettings;

        public void StoreValue(string key, object value)
        {
            try
            {
                if (_localSettings.Contains(key) == false)
                {
                    _localSettings.Add(key, value);
                }
                else
                {
                    _localSettings[key] = value;
                }
                _localSettings.Save();
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
                if (_localSettings.Contains(key) == true)
                {
                    return _localSettings[key];
                }
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
                if (_localSettings.Contains(key) == true)
                {
                    _localSettings.Remove(key);
                    _localSettings.Save();
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}
