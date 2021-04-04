using System.Threading;

namespace CapitalGateway
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            Thread.CurrentThread.Name = "Main";
            
            CapitalApi capitalApi = new CapitalApi();
            new TcpServer(capitalApi);
        }
    }
}