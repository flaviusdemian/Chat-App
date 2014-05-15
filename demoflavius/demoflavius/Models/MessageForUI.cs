using Coding4Fun.Toolkit.Controls;
using Windows.UI.Xaml;

namespace demoflavius_W8.Models
{
    public class MessageForUI
    {
        public string Content { get; set; }

        public string FromId { get; set; }

        public ChatBubbleDirection ChatBubbleDirection { get; set; }

        public HorizontalAlignment Alignment { get; set; }
    }
}