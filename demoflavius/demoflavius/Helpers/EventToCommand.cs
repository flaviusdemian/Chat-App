using System;
using System.Windows.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;

namespace demoflavius_W8.Helpers
{
    public static class EventToCommand
    {
        public enum EventKind
        {
            Tapped = 0,
            TextChanged = 1,
            SelectionChanged = 2,
            None = -1,
        }

        /// <summary>
        ///     The CommandParameter attached property's name.
        /// </summary>
        public const string CommandParameterPropertyName = "CommandParameter";

        /// <summary>
        ///     The Command attached property's name.
        /// </summary>
        public const string CommandPropertyName = "Command";

        /// <summary>
        ///     The Event attached property's name.
        /// </summary>
        public const string EventPropertyName = "Event";

        /// <summary>
        ///     The PassEventArgs attached property's name.
        /// </summary>
        public const string PassEventArgsPropertyName = "PassEventArgs";

        /// <summary>
        ///     Identifies the CommandParameter attached property.
        /// </summary>
        public static readonly DependencyProperty CommandParameterProperty = DependencyProperty.RegisterAttached(
            CommandParameterPropertyName,
            typeof (object),
            typeof (EventToCommand),
            new PropertyMetadata(null));

        /// <summary>
        ///     Identifies the Command attached property.
        /// </summary>
        public static readonly DependencyProperty CommandProperty = DependencyProperty.RegisterAttached(
            CommandPropertyName,
            typeof (ICommand),
            typeof (EventToCommand),
            new PropertyMetadata(
                null));

        /// <summary>
        ///     Identifies the Event attached property.
        /// </summary>
        public static readonly DependencyProperty EventProperty = DependencyProperty.RegisterAttached(
            EventPropertyName,
            typeof (EventKind),
            typeof (EventToCommand),
            new PropertyMetadata(
                string.Empty,
                (s, e) => AttachEvent(s, (EventKind) e.NewValue)));

        /// <summary>
        ///     Identifies the PassEventArgs attached property.
        /// </summary>
        public static readonly DependencyProperty PassEventArgsProperty = DependencyProperty.RegisterAttached(
            PassEventArgsPropertyName,
            typeof (bool),
            typeof (EventToCommand),
            new PropertyMetadata(false));

        /// <summary>
        ///     Gets the value of the PassEventArgs attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object for which the property value
        ///     is read.
        /// </param>
        /// <returns>The value of the PassEventArgs property of the specified object.</returns>
        public static bool GetPassEventArgs(DependencyObject obj)
        {
            return (bool) obj.GetValue(PassEventArgsProperty);
        }

        /// <summary>
        ///     Sets the value of the PassEventArgs attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object to which the property value
        ///     is written.
        /// </param>
        /// <param name="value">Sets the PassEventArgs value of the specified object.</param>
        public static void SetPassEventArgs(DependencyObject obj, bool value)
        {
            obj.SetValue(PassEventArgsProperty, value);
        }

        /// <summary>
        ///     Gets the value of the Command attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object for which the property value
        ///     is read.
        /// </param>
        /// <returns>The value of the Command property of the specified object.</returns>
        public static ICommand GetCommand(DependencyObject obj)
        {
            return (ICommand) obj.GetValue(CommandProperty);
        }

        /// <summary>
        ///     Gets the value of the CommandParameter attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object for which the property value
        ///     is read.
        /// </param>
        /// <returns>The value of the CommandParameter property of the specified object.</returns>
        public static object GetCommandParameter(DependencyObject obj)
        {
            return obj.GetValue(CommandParameterProperty);
        }

        /// <summary>
        ///     Gets the value of the Event attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object for which the property value
        ///     is read.
        /// </param>
        /// <returns>The value of the Event property of the specified object.</returns>
        public static EventKind GetEvent(DependencyObject obj)
        {
            return (EventKind) obj.GetValue(EventProperty);
        }

        /// <summary>
        ///     Sets the value of the Command attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object to which the property value
        ///     is written.
        /// </param>
        /// <param name="value">Sets the Command value of the specified object.</param>
        public static void SetCommand(DependencyObject obj, ICommand value)
        {
            obj.SetValue(CommandProperty, value);
        }

        /// <summary>
        ///     Sets the value of the CommandParameter attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object to which the property value
        ///     is written.
        /// </param>
        /// <param name="value">Sets the CommandParameter value of the specified object.</param>
        public static void SetCommandParameter(DependencyObject obj, object value)
        {
            obj.SetValue(CommandParameterProperty, value);
        }

        /// <summary>
        ///     Sets the value of the Event attached property
        ///     for a given dependency object.
        /// </summary>
        /// <param name="obj">
        ///     The object to which the property value
        ///     is written.
        /// </param>
        /// <param name="value">Sets the Event value of the specified object.</param>
        public static void SetEvent(DependencyObject obj, EventKind value)
        {
            obj.SetValue(EventProperty, value);
        }

        private static void AttachEvent(DependencyObject owner, EventKind kind)
        {
            var sender = owner as UIElement;
            if (sender == null)
            {
                return;
            }

            switch (kind)
            {
                case EventKind.Tapped:
                    sender.AddHandler(UIElement.TappedEvent, (TappedEventHandler) ExecuteCommand, true);
                    break;

                case EventKind.TextChanged:
                    var text = sender as TextBox;
                    if (text != null)
                    {
                        text.TextChanged += TextChanged;
                    }
                    break;

                case EventKind.SelectionChanged:

                    var control = sender as Selector;
                    if (control != null)
                    {
                        control.SelectionChanged += ControlSelectionChanged;
                    }

                    break;

                default:
                    throw new NotSupportedException("Unsupported event");
            }
        }

        private static void TextChanged(object s, TextChangedEventArgs e)
        {
            var sender = s as TextBox;
            if (sender == null)
            {
                return;
            }

            ICommand command = GetCommand(sender);
            if (command != null)
            {
                object parameter = GetCommandParameter(sender);
                command.Execute(parameter);
            }
        }

        private static void ControlSelectionChanged(object s, SelectionChangedEventArgs e)
        {
            var sender = s as UIElement;
            if (sender == null)
            {
                return;
            }

            ICommand command = GetCommand(sender);
            if (command != null)
            {
                if (GetPassEventArgs(sender))
                {
                    command.Execute(e);
                }
                else
                {
                    object parameter = GetCommandParameter(sender);
                    command.Execute(parameter);
                }
            }
        }

        private static void ExecuteCommand(object s, RoutedEventArgs e)
        {
            var sender = s as UIElement;
            if (sender == null)
            {
                return;
            }

            ICommand command = GetCommand(sender);
            if (command != null)
            {
                if (GetPassEventArgs(sender))
                {
                    command.Execute(e);
                }
                else
                {
                    object parameter = GetCommandParameter(sender);
                    command.Execute(parameter);
                }
            }
        }
    }
}