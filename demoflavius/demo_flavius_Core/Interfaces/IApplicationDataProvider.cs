using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace demo_flavius_Core.Interfaces
{
    public interface IApplicationDataProvider
    {
       void StoreValue(string key, object value);

       object GetValue(string key);

       void RemoveEntry(string key);
    }
}
