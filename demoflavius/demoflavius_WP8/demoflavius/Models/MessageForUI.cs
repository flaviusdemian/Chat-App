using Coding4Fun.Toolkit.Controls;
using System.Windows;

namespace demoflavius.Models
{
    public class MessageForUI
    {
        public string Content { get; set; }

        public string FromId { get; set; }

        public ChatBubbleDirection ChatBubbleDirection { get; set; }

        public HorizontalAlignment Alignment { get; set; }
    }
}